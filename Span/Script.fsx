#load "Utils.fs"
open Shaftesbury.FSharp.Utils
#load "Span2.XMLParserDataTypes.fs"
open Shaftesbury.Span.XML2.ParserDataTypes
#load "Span2.XMLParser.fs"
open Shaftesbury.Span.XML2.Parser
#load "Span2.QueryTools.fs"
open Shaftesbury.Span.XML2.QueryTools

let XMLfilenames = 
    [
        "ASXCLFEndOfDayRiskParameterFile130306.spn";
        "CFDEndOfDayRiskParameterFile120720.spn";
        "NZFEndOfDayRiskParameterFile130306.spn";
        "SFEEndOfDayRiskParameterFile130128.spn"
    ] |> List.map (fun nm -> @"C:\Users\Bob\development\data\Span\"+nm)

let trees = XMLfilenames |> List.map prepareXMLFile |> List.map readXML

let fn (tLeg:SpanXMLTLeg) = tLeg.Cc = "HV"
    
findNodeWithPath (tLegNode fn) trees.[0]

// We want to find the node which satisifies
// max{/spanFile/pointInTime/clearingOrg/exchange/ooePf[undPf[pfCode="ANZ"] and exercise="AMER"]/series[setlDate="20130327"]/opt[o="C" and k="2850.00"]/ra/a

// Predicates for above filter functions
let undPf_pfCode pfCode (undPf : SpanXMLUndPf) = undPf.PfCode=pfCode
let oofPf_exercise exercise (o: SpanXMLOofPf) = o.Exercise=exercise

// americanOptions contains all the nodes which satisfy the oofPf_exercise predicate
let americanOptions = trees |> List.collect (fun tree -> findNodeWithPath (oofPfNode (oofPf_exercise "AMER")) tree)
// filter americanOptions including those nodes where pfCode is "BB"
let BB = americanOptions |> List.filter (fun (node,path) -> findNode (undPfNode (undPf_pfCode "BB")) node |> List.isEmpty |> not)

// Predicate for SpanXMLSeries SetlDate
let seriesSetlDate dt (s:SpanXMLSeries) = s.SetlDate=dt

// filter BB including those nodes where series has SetlDate of 20130313
let series = BB |> List.filter (fun (node,path) -> findNode (seriesNode (seriesSetlDate 20130313)) node |> List.isEmpty |> not)

let optO o (opt : SpanXMLOpt) = opt.O = o
let optK k (opt : SpanXMLOpt) = opt.K = k

// All the supplied predicates must evaluate to 'true'
let optCombination lst (opt:SpanXMLOpt) = lst |> List.forall (fun predicate -> predicate opt)

// filter series to include only the nodes which satisfy the list of predicates
let opt = series |> List.filter (fun (node,path) -> findNode (optNode (optCombination [(optO "C"); (optK 95.5);])) node |> List.isEmpty |> not)

// Get Max scenario from Risk Array
// Max{/spanFile/pointInTime/clearingOrg/exchange/ooePf[undPf[pfCode="ANZ"] and exercise="AMER"]/series[setlDate="20130327"]/opt[o="C" and k="2850.00"]/ra/a
opt |> List.map first |> findMaxScenario

// 2a
// Maximum of the SPAN Risk Array as described in Step1. The trade facts include;
// CONTRACTYEARMONTH=201309 and SYMBOL=BB
// max(/spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201309"]/ra/a)
// max(/spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201312"]/ra/a)

let futPf = trees |> 
            List.collect (fun tree -> findNode (futPfNode (fun futpf -> futpf.PfCode="BB")) tree) |>
            List.filter (fun tree -> findNode (futNode (fun f -> f.Pe=201309 || f.Pe=201312)) tree |> List.isEmpty |> not)

findMaxScenario futPf

// 2b
// Composite Delta (Δ) using the following query.
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201309"]/d
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201312"]/d

futPf |> 
    List.collect (fun tree -> findNode (futNode (fun f -> true)) tree) |> 
    List.choose (fun f ->
        match f with 
        | Node(SpanXMLFut(record),_) -> Some record.D
        | _ -> None)


// 2c
// Lot size (Lots) using the following query:
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201309”]/undC/i [needs verification]
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201312”]/undC/i
// The results are 1 and 1.
 
futPf |> 
    List.collect (fun tree -> findNode (undCNode (fun t -> true)) tree) |> 
    List.choose (fun node -> 
        match node with
        | Node (SpanXMLUndC(record),_) -> Some record.I
        | _ -> None)

// 2d.
// Retrieve the Delta Divisor using the following query:
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/cvf [needs verification]
// The result is 0.

trees |> 
    List.collect (fun tree -> findNode (futPfNode (fun futpf -> futpf.PfCode="BB")) tree) |> 
    List.choose (fun node ->
        match node with
        | Node (SpanXMLFutPf(record),_) -> Some record.Cvf
        | _ -> None)


// 3. Retrieve Inter-prompt Spread Code.
// a.
// Retrieve the intra-tier using the start date. For the two trades shown it should Sep13 and Dec13.
// /spanFile/pointInTime/clearingOrg/ccDef[cc="BB"]/intraTiers/tier[sPe="201309"]/tn
// /spanFile/pointInTime/clearingOrg/ccDef[cc="BB"]/intraTiers/tier[sPe="201312"]/tn
// The results are 3 and 4, respectively.
 
let ccDefs = trees |> List.collect (fun tree -> findNode (ccDefNode (fun ccDef -> ccDef.Cc="BB")) tree)
let intraTiers = ccDefs |> List.collect (fun tree -> findNode (intraTiersNode (fun f -> true)) tree)

intraTiers |> 
    List.collect (fun tree -> findNode (tierNode (fun tier -> tier.SPe=Some 201309 || tier.SPe=Some 201312)) tree) |> 
    List.choose (fun node -> 
        match node with
        | Node (SpanXMLTier(record),_) -> Some record.Tn
        | _ -> None)

// b.        Using the tiers, find the respective Spread Charge
// /spanFile/pointInTime/clearingOrg/ccDef[cc="BB"]/dSpread[tLeg/tn=3 and tLeg/tn=4]/rate/val
// The result is 120.

let tLegNodeTn n (tLeg:SpanXMLTLeg) = tLeg.Tn = n
let tLegCombo (tLeg:SpanXMLTLeg) = (tLegNodeTn 3 tLeg) || (tLegNodeTn 4 tLeg)

let dSpreads = ccDefs |>
                List.collect (fun tree -> findNode (dSpreadNode (fun t -> true)) tree) |> 
                List.filter (fun tree -> findNode (tLegNode tLegCombo) tree |> List.isEmpty |> not)

dSpreads |> 
    List.collect (fun tree -> findNode (rateNode (fun t -> true)) tree) |> 
    List.choose (fun node ->
        match node with
        | Node (SpanXMLRate(record),_) -> Some record.Val
        | _ -> None)
 
// 4.        Calculate Inter-prompt Spread Charge
//
//a.     Multiple ‘CURRENTQUANTITY’, Δ and Lots, divide by (1-Delta Divisor) for each trade then add both. Multiply by the Inter-Prompt Spread Charge.
//
//(-40x1x1 + 100x1x1) x 120 = $7,200 with a Delta remainder of 60.
// b.      Calculate Spot Charges



// 5.        For each trade, retrieve the:
// 5a.        Spot Charge for the tier in question (note this example on has one tier);
// SYMBOL=BB
// /spanFile/pointInTime/clearingOrg/ccDef[cc="BB"]/somTiers/tier/rate/val
// The result is 12

ccDefs |> 
    List.collect (fun tree -> findNode (somTiersNode (fun t -> true)) tree) |> 
    List.collect (fun tree -> findNode (tierNode (fun t -> true)) tree) |> 
    List.collect (fun tree -> findNode (rateNode (fun t -> true)) tree) |> 
    List.choose (fun node ->
        match node with
        | Node (SpanXMLRate(rate),_) -> Some rate.Val
        | _ -> None)


// 6. The Prompt Charges is calculated by multiplying the Prompt Charge by the remaining Delta. Continuing from the previous example it is:
// 60 x 12 = $720
// 6a. Calculate inter-contract spread charges

// 7. For each trade pair, retrieve the Credit Spread Charge using the following query:
// /spanFile/pointInTime/clearingOrg/interSpreads/dSpread[tLeg[cc="ANZ"] and tLeg[cc="CBA"]]/rate/val
// The result is 0.4

let tLegCombo2 (tLeg:SpanXMLTLeg) = (tLeg.Cc = "EE") || (tLeg.Cc = "PQ")

trees |> 
    List.collect (fun tree -> findNode (dSpreadNode (fun t -> true)) tree) |> 
    List.filter (fun tree -> findNode (tLegNode tLegCombo2) tree |> List.isEmpty |> not) |>
    List.collect (fun tree -> findNode (rateNode (fun t -> true)) tree) |> 
    List.choose (fun node ->
        match node with
        | Node (SpanXMLRate(rate),_) -> Some rate.Val
        | _ -> None)

// 8. Multiply A. by the Span Risk Array worst case found in Step 1.
// Span Risk Array worst case is given by:
// max(/spanFile/pointInTime/clearingOrg/exchange/ooePf[undPf[pfCode="ANZ"] and exercise="AMER"]/series[setlDate="20130327"]/opt[o="C" and k="2850.00"]/ra/a)
// The result is 768
// 0.4 x 768 = $307.20 credit.

// 9. Repeat Steps A & B for all pairs for any remaining delta.



// Given a portfolio of instruments and their quantities and a list of possible ways by which instruments may be matched together with the
// proportions matchable, calculate the reduced portfolio and show the portions which were removed. For example, if we have 10 of A and 5 of B
// and the correlations state that 1 of A corresponds to 2 of B then we can reduce the portfolio for consideration (in the margining process) to
// 7.5 of A and 0 of B, assuming non-integral positions are allowable.

#load "SpanProcess.fs"
open Shaftesbury.Span.SpanProcess

let portfolio = ["ANZ", 10; "BASF", 1250; "FTSE100", -1000; "BB", -25] |> List.map (fun (inst,q) -> inst, float q)
let matchingSets = [
    ["ANZ",1.0; "BB", 1.0;];
    ["ANZ",0.75; "FTSE100",1.0;];
    ["ANZ", 1.0; "BASF", 0.1; "BB", 5.0];
    ["BB", 1.0; "FTSE100", 1.0];
    ]

let remainingPosns, reductions = findNonOffsettingInsts portfolio matchingSets
