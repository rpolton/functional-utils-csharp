#load "Utils.fs"
open Shaftesbury.FSharp.Utils

let defaults = ["int","0";"float","0.0";"string","\"\"";"int64","0L"]
let initialDictEntry (fieldName, fieldTypeName) = "\""+fieldName+"\"," + (defaults |> List.find (fun (typ,def) -> typ=fieldTypeName) |> second) + ":>obj; "
let readFn typ = ["int","readAsInt";"float","readAsFloat";"string","readAsString";"int64","readAsInt64"] |> List.find (fun (t,fn)->t=typ) |> second
let matchEntry (fieldName, fieldTypeName) = @"        |"+" \""+fieldName+"\" as name -> dict.[name] <- "+(readFn fieldTypeName)+" reader ; read ()"
let recordEntry (fieldName, fieldTypeName) = "                "+(capitalise fieldName)+" = dict.[\""+fieldName+"\"] :?> "+fieldTypeName
 
// fields is a list of (fieldName, fieldTypeName)
// It is assumed that the XML structure field name is the capitalised fieldName
let generate name fields =
    let dictDefn = fields |> List.rev |> List.fold (fun st field -> initialDictEntry field + st) ""
    [
        "let read"+name+" (reader:System.Xml.XmlReader) : nodeType * tree list =";
        "    let dict = ["+ dictDefn + @"] |> toDict";
        "";
        "    let rec read () =";
        "        match reader.Name with";
    ]  @ (fields |> List.map matchEntry) @
    [
        "        | _ -> ignore ()";
        "    read ()";
        "    SpanXML"+name+"(";
        "        {";
    ] @ (fields |> List.map recordEntry) @
    [
        "        }), []";
        "";
    ]
 
let generateSomeCode (namesAndFields:((string*(string*string) list) list)) =
    use fs = new System.IO.StreamWriter (@"C:\Users\Bob\development\data\generatedCode")
    namesAndFields |> List.map (fun (name,fields) -> generate name fields) |> List.concat |> List.iter fs.WriteLine
    fs.Close()

[
"Div",["val","float";"dtm","int";"setlDate","int"];
"Ra",["r","int";"a","float";"d","float"];
"DivRate",["val","float";];
"UndC",["exch","string";"pfId","int";"cId","int";"s","string";"i","float"];
"IntrRate",["val","float";"rl","int";"cpm","int";"exm","int"];
"Opt",["cId","int";"o","string";"k","float";"p","float";"pq","int";"d","float";"v","float";"val","float";"cvf","float";"svf","float"];
"Rate",["r","int";"val","float"];
"ScanRate",["r","int";"priceScan","float";"priceScanPct","float";"volScan","float";"volScanPct","float"];
"PriceScanDef",["mult","float";"numerator","float";"denominator","float"];
"VolScanDef",["mult","float";"numerator","float";"denominator","float"];
"Phy",["cId","int";"pe","string";"p","float";"d","float";"v","float";"cvf","float";"val","float";"sc","float";];
"Group",["id","int";"aval","string"];
"Equity",["cId","int";"isin","string";"pe","string";"p","float";"d","float";"v","float";"cvf","float";"val","float";"sc","float";"desc","string";"type","string";"subType","string";];
"UndPf",["exch","string";"pfId","int";"pfCode","string";"pfType","string";"s","string";"i","float"];
"Fut",["cId","int";"pe","int";"p","float";"d","float";"v","float";"cvf","float";"val","float";"sc","float";"setlDate","int";"t","float"];
"Series",["pe","int";"v","float";"volSrc","string";"setlDate","int";"t","float";"cvf","float";"svf","float";"sc","float";];
"Tier",["tn","int";"ePe","int";"sPe","int"];
"TierWithRate",["tn","int"];
"TierWithScanRate",["tn","int";"ePe","int";"sPe","int"];
"TLeg",["cc","string";"tn","int";"rs","string";"i","float"];
"PLeg",["cc","string";"pe","int";"rs","string";"i","float"];
"SLeg",["cc","string";"isTarget","int";"isRequired","int"];
"ScanPointDef",["point","int";"weight","float";"pairedPoint","int"];
"DeltaPointDef",["point","int";"weight","float";];
"PhyPf",["pfId","int";"pfCode","string";"name","string";"currency","string";"cvf","float";"priceDl","int";"priceFmt","string";"valueMeth","string";"priceMeth","string";"setlMeth","string";"positionsAllowed","int"];
"EquityPf",["pfId","string";"pfCode","string";"name","string";"currency","string";"cvf","float";"priceDl","int";"priceFmt","string";"valueMeth","string";"priceMeth","string";"setlMeth","string";"country","string"];
"FutPf",["pfId","string";"pfCode","string";"name","string";"currency","string";"cvf","float";"priceDl","int";
"priceFmt","string";"valueMeth","string";"priceMeth","string";"setlMeth","string";"positionsAllowed","int"];
"OopPf",["pfId","string";"pfCode","string";"name","string";"currency","string";"cvf","float";"priceDl","int";
        "priceFmt","string";"strikeDl","int";"strikeFmt","string";"cab","float";"valueMeth","string";
        "priceMeth","string";"setlMeth","string";"priceModel","string"];
"OofPf",["pfId","string";"pfCode","string";"name","string";"exercise","string";
        "currency","string";"cvf","float";"priceDl","int";"priceFmt","string";"strikeDl","int";"strikeFmt","string";
        "cab","float";"valueMeth","string";"priceMeth","string";"setlMeth","string";"priceModel","string";
        "isVariableTick","int"];
"OoePf",["pfId","string";"pfCode","string";"name","string";"exercise","string";"currency","string";"cvf","float";"priceDl","int";
        "priceFmt","string";"strikeDl","int";"strikeFmt","string";"cab","float";"valueMeth","string";
        "priceMeth","string";"setlMeth","string";"priceModel","string"];
"PfLink",["exch","string";"pfId","int";"pfCode","string";"pfType","string";"sc","float";"cmbMeth","string";"applyBasisRisk","int";"oopDeltaMeth","string"];
"DSpread",["spread","int";"chargeMeth","string"];
"HSpread",["spread","int";];
"SpotRate",["r","int";"pe","int";"sprd","float";"outr","float"];
"CurConv",["fromCur","string";"toCur","string";"factor","float"];
"PbRateDef",["r","int";"isCust","int";"acctType","string";"isM","int";"pbc","string"];
"PointDef",["r","int";];
"Exchange",["exch","string";"name","string";];
"CcDef",["cc","string";"name","string";"currency","string";"riskExponent","int";"capAnov","int";
        "procMeth","string";"wfprMeth","string";"spotMeth","string";"somMeth","string";"cmbMeth","string";
        "marginMeth","string";"factorCurveSetId","int";"factorScenarioSetId","int";
        "interCurScan","int";"limitArraysTo16Points","int";];
"CurrencyDef",["currency","string";"symbol","string";"name","string";"decimalPos","int"];
"AcctTypeDef",["isCust","int";"acctType","string";"name","string";"isNetMargin","int";"priority","int"];
"AcctSubTypeDef",["acctSubTypeCode","string";"dataType","string";"description","string"];
"GroupTypeDef",["id","int";"name","string";];
"GroupDef",["id","int";"aval","string";"description","string"];
"ClearingOrg",["ec","string";"name","string";"isContractScale","int";"isNetMargin","int";"finalizeMeth","string";
        "oopDeltaMeth","string";"capAnov","int";"lookAheadYears","float";"daysPerYear","int";
        "limitSubAccountOffset","int";"lookAheadDays","int";];
"PointInTime",["date","int";"isSetl","int";"setlQualifier","string"];
"Level2",["fileFormat","string";"created","int64"];
]
|> generateSomeCode

