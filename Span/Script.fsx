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
let americanOptions = trees |> List.map (fun tree -> findNodeWithPath (oofPfNode (oofPf_exercise "AMER")) tree) |> List.concat
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
    List.map (fun tree -> findNode (futPfNode (fun futpf -> futpf.PfCode="BB")) tree) |> List.concat |>
    List.filter (fun tree -> findNode (futNode (fun f -> f.Pe=201309 || f.Pe=201312)) tree |> List.isEmpty |> not)

findMaxScenario futPf

// 2b
// Composite Delta (Δ) using the following query.
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201309"]/d
// /spanFile/pointInTime/clearingOrg/exchange/futPf[pfCode=”BB”]/fut[pe="201312"]/d

futPf |> List.map (fun tree -> findNode (futNode (fun f -> true)) tree) |> List.concat |> 
    List.choose (fun f ->
        match f with 
        | Node(SpanXMLFut(record),_) -> Some record.D
        | _ -> None)
