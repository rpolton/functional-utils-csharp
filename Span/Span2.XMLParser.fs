module Shaftesbury.Span2.XMLParser

open Shaftesbury.Span2.XMLParserDataTypes
open Shaftesbury.FSharp.Utils

let readAsFloat (reader:System.Xml.XmlReader) = reader.ReadElementContentAsDouble()
let readAsInt (reader:System.Xml.XmlReader) = reader.ReadElementContentAsInt()
let readAsInt64 (reader:System.Xml.XmlReader) = reader.ReadElementContentAsLong()
let readAsString (reader:System.Xml.XmlReader) = reader.ReadElementContentAsString()

let toDict = toDictionary first second

let readDiv (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["val",0.0:>obj; "dtm",0:>obj; "setlDate",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "dtm" as name -> dict.[name] <- readAsInt reader ; read ()
        | "setlDate" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLDiv(
        {
                Val = dict.["val"] :?> float
                Dtm = dict.["dtm"] :?> int
                SetlDate = dict.["setlDate"] :?> int
        }), []

let readRa (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "a",0.0:>obj; "d",0.0:>obj; ] |> toDict

    let rec read a =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read a
        | "a" as name -> read ((readAsFloat reader) :: a)
        | "d" as name -> dict.[name] <- readAsFloat reader ; read a
        | _ -> a
    let a = read []
    SpanXMLRa(
        {
                R = dict.["r"] :?> int
                A = a
                D = dict.["d"] :?> float
        }), []

let readDivRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["val",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLDivRate(
        {
                Val = dict.["val"] :?> float
        }), []

let readUndC (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["exch","":>obj; "pfId",0:>obj; "cId",0:>obj; "s","":>obj; "i",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "exch" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "s" as name -> dict.[name] <- readAsString reader ; read ()
        | "i" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLUndC(
        {
                Exch = dict.["exch"] :?> string
                PfId = dict.["pfId"] :?> int
                CId = dict.["cId"] :?> int
                S = dict.["s"] :?> string
                I = dict.["i"] :?> float
        }), []

let readIntrRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["val",0.0:>obj; "rl",0:>obj; "cpm",0:>obj; "exm",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "rl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "cpm" as name -> dict.[name] <- readAsInt reader ; read ()
        | "exm" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLIntrRate(
        {
                Val = dict.["val"] :?> float
                Rl = dict.["rl"] :?> int
                Cpm = dict.["cpm"] :?> int
                Exm = dict.["exm"] :?> int
        }), []

let readOpt (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "o","":>obj; "k",0.0:>obj; "p",0.0:>obj; "pq",0:>obj; "d",0.0:>obj; "v",0.0:>obj; "val",0.0:>obj; "cvf",0.0:>obj; "svf",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "o" as name -> dict.[name] <- readAsString reader ; read ()
        | "k" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "p" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "pq" as name -> dict.[name] <- readAsInt reader ; read ()
        | "d" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "svf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLOpt(
        {
                CId = dict.["cId"] :?> int
                O = dict.["o"] :?> string
                K = dict.["k"] :?> float
                P = dict.["p"] :?> float
                Pq = dict.["pq"] :?> int
                D = dict.["d"] :?> float
                V = dict.["v"] :?> float
                Val = dict.["val"] :?> float
                Cvf = dict.["cvf"] :?> float
                Svf = dict.["svf"] :?> float
        }), []

let readRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "val",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLRate(
        {
                R = dict.["r"] :?> int
                Val = dict.["val"] :?> float
        }), []

let readScanRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "priceScan",0.0:>obj; "priceScanPct",0.0:>obj; "volScan",0.0:>obj; "volScanPct",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceScan" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceScanPct" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "volScan" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "volScanPct" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLScanRate(
        {
                R = dict.["r"] :?> int
                PriceScan = dict.["priceScan"] :?> float
                PriceScanPct = dict.["priceScanPct"] :?> float
                VolScan = dict.["volScan"] :?> float
                VolScanPct = dict.["volScanPct"] :?> float
        }), []

let readPriceScanDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["mult",0.0:>obj; "numerator",0.0:>obj; "denominator",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "mult" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "numerator" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "denominator" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPriceScanDef(
        {
                Mult = dict.["mult"] :?> float
                Numerator = dict.["numerator"] :?> float
                Denominator = dict.["denominator"] :?> float
        }), []

let readVolScanDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["mult",0.0:>obj; "numerator",0.0:>obj; "denominator",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "mult" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "numerator" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "denominator" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLVolScanDef(
        {
                Mult = dict.["mult"] :?> float
                Numerator = dict.["numerator"] :?> float
                Denominator = dict.["denominator"] :?> float
        }), []

let readPhy (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "pe","":>obj; "p",0.0:>obj; "d",0.0:>obj; "v",0.0:>obj; "cvf",0.0:>obj; "val",0.0:>obj; "sc",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pe" as name -> dict.[name] <- readAsString reader ; read ()
        | "p" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "d" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPhy(
        {
                CId = dict.["cId"] :?> int
                Pe = dict.["pe"] :?> string
                P = dict.["p"] :?> float
                D = dict.["d"] :?> float
                V = dict.["v"] :?> float
                Cvf = dict.["cvf"] :?> float
                Val = dict.["val"] :?> float
                Sc = dict.["sc"] :?> float
        }), []

let readGroup (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["id",0:>obj; "aval","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "id" as name -> dict.[name] <- readAsInt reader ; read ()
        | "aval" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLGroup(
        {
                Id = dict.["id"] :?> int
                Aval = dict.["aval"] :?> string
        }), []

let readEquity (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "isin","":>obj; "pe","":>obj; "p",0.0:>obj; "d",0.0:>obj; "v",0.0:>obj; "cvf",0.0:>obj; "val",0.0:>obj; "sc",0.0:>obj; "desc","":>obj; "type","":>obj; "subType","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "isin" as name -> dict.[name] <- readAsString reader ; read ()
        | "pe" as name -> dict.[name] <- readAsString reader ; read ()
        | "p" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "d" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "desc" as name -> dict.[name] <- readAsString reader ; read ()
        | "type" as name -> dict.[name] <- readAsString reader ; read ()
        | "subType" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLEquity(
        {
                CId = dict.["cId"] :?> int
                Isin = dict.["isin"] :?> string
                Pe = dict.["pe"] :?> string
                P = dict.["p"] :?> float
                D = dict.["d"] :?> float
                V = dict.["v"] :?> float
                Cvf = dict.["cvf"] :?> float
                Val = dict.["val"] :?> float
                Sc = dict.["sc"] :?> float
                Desc = dict.["desc"] :?> string
                Type = dict.["type"] :?> string
                SubType = dict.["subType"] :?> string
        }), []

let readUndPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["exch","":>obj; "pfId",0:>obj; "pfCode","":>obj; "pfType","":>obj; "s","":>obj; "i",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "exch" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfType" as name -> dict.[name] <- readAsString reader ; read ()
        | "s" as name -> dict.[name] <- readAsString reader ; read ()
        | "i" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLUndPf(
        {
                Exch = dict.["exch"] :?> string
                PfId = dict.["pfId"] :?> int
                PfCode = dict.["pfCode"] :?> string
                PfType = dict.["pfType"] :?> string
                S = dict.["s"] :?> string
                I = dict.["i"] :?> float
        }), []

let readFut (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "pe",0:>obj; "p",0.0:>obj; "d",0.0:>obj; "v",0.0:>obj; "cvf",0.0:>obj; "val",0.0:>obj; "sc",0.0:>obj; "setlDate",0:>obj; "t",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "p" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "d" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "setlDate" as name -> dict.[name] <- readAsInt reader ; read ()
        | "t" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLFut(
        {
                CId = dict.["cId"] :?> int
                Pe = dict.["pe"] :?> int
                P = dict.["p"] :?> float
                D = dict.["d"] :?> float
                V = dict.["v"] :?> float
                Cvf = dict.["cvf"] :?> float
                Val = dict.["val"] :?> float
                Sc = dict.["sc"] :?> float
                SetlDate = dict.["setlDate"] :?> int
                T = dict.["t"] :?> float
        }), []

let readSeries (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pe",0:>obj; "v",0.0:>obj; "volSrc","":>obj; "setlDate",0:>obj; "t",0.0:>obj; "cvf",0.0:>obj; "svf",0.0:>obj; "sc",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "volSrc" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlDate" as name -> dict.[name] <- readAsInt reader ; read ()
        | "t" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "svf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLSeries(
        {
                Pe = dict.["pe"] :?> int
                V = dict.["v"] :?> float
                VolSrc = dict.["volSrc"] :?> string
                SetlDate = dict.["setlDate"] :?> int
                T = dict.["t"] :?> float
                Cvf = dict.["cvf"] :?> float
                Svf = dict.["svf"] :?> float
                Sc = dict.["sc"] :?> float
        }), []

let readTier (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["tn",0:>obj; "ePe",0:>obj; "sPe",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "tn" as name -> dict.[name] <- readAsInt reader ; read ()
        | "ePe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "sPe" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLTier(
        {
                Tn = dict.["tn"] :?> int
                EPe = dict.["ePe"] :?> int
                SPe = dict.["sPe"] :?> int
        }), []

let readTierWithRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["tn",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "tn" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLTierWithRate(
        {
                Tn = dict.["tn"] :?> int
        }), []

let readTierWithScanRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["tn",0:>obj; "ePe",0:>obj; "sPe",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "tn" as name -> dict.[name] <- readAsInt reader ; read ()
        | "ePe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "sPe" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLTierWithScanRate(
        {
                Tn = dict.["tn"] :?> int
                EPe = dict.["ePe"] :?> int
                SPe = dict.["sPe"] :?> int
        }), []

let readTLeg (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cc","":>obj; "tn",0:>obj; "rs","":>obj; "i",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cc" as name -> dict.[name] <- readAsString reader ; read ()
        | "tn" as name -> dict.[name] <- readAsInt reader ; read ()
        | "rs" as name -> dict.[name] <- readAsString reader ; read ()
        | "i" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLTLeg(
        {
                Cc = dict.["cc"] :?> string
                Tn = dict.["tn"] :?> int
                Rs = dict.["rs"] :?> string
                I = dict.["i"] :?> float
        }), []

let readPLeg (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cc","":>obj; "pe",0:>obj; "rs","":>obj; "i",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cc" as name -> dict.[name] <- readAsString reader ; read ()
        | "pe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "rs" as name -> dict.[name] <- readAsString reader ; read ()
        | "i" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPLeg(
        {
                Cc = dict.["cc"] :?> string
                Pe = dict.["pe"] :?> int
                Rs = dict.["rs"] :?> string
                I = dict.["i"] :?> float
        }), []

let readSLeg (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cc","":>obj; "isTarget",0:>obj; "isRequired",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cc" as name -> dict.[name] <- readAsString reader ; read ()
        | "isTarget" as name -> dict.[name] <- readAsInt reader ; read ()
        | "isRequired" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLSLeg(
        {
                Cc = dict.["cc"] :?> string
                IsTarget = dict.["isTarget"] :?> int
                IsRequired = dict.["isRequired"] :?> int
        }), []

let readScanPointDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["point",0:>obj; "weight",0.0:>obj; "pairedPoint",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "point" as name -> dict.[name] <- readAsInt reader ; read ()
        | "weight" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "pairedPoint" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLScanPointDef(
        {
                Point = dict.["point"] :?> int
                Weight = dict.["weight"] :?> float
                PairedPoint = dict.["pairedPoint"] :?> int
        }), []

let readDeltaPointDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["point",0:>obj; "weight",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "point" as name -> dict.[name] <- readAsInt reader ; read ()
        | "weight" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLDeltaPointDef(
        {
                Point = dict.["point"] :?> int
                Weight = dict.["weight"] :?> float
        }), []

let readPhyPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId",0:>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "positionsAllowed",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "positionsAllowed" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPhyPf(
        {
                PfId = dict.["pfId"] :?> int
                PfCode = dict.["pfCode"] :?> string
                Name = dict.["name"] :?> string
                Currency = dict.["currency"] :?> string
                Cvf = dict.["cvf"] :?> float
                PriceDl = dict.["priceDl"] :?> int
                PriceFmt = dict.["priceFmt"] :?> string
                ValueMeth = dict.["valueMeth"] :?> string
                PriceMeth = dict.["priceMeth"] :?> string
                SetlMeth = dict.["setlMeth"] :?> string
                PositionsAllowed = dict.["positionsAllowed"] :?> int
        }), []

let readEquityPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "country","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "country" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLEquityPf(
        {
                PfId = dict.["pfId"] :?> string
                PfCode = dict.["pfCode"] :?> string
                Name = dict.["name"] :?> string
                Currency = dict.["currency"] :?> string
                Cvf = dict.["cvf"] :?> float
                PriceDl = dict.["priceDl"] :?> int
                PriceFmt = dict.["priceFmt"] :?> string
                ValueMeth = dict.["valueMeth"] :?> string
                PriceMeth = dict.["priceMeth"] :?> string
                SetlMeth = dict.["setlMeth"] :?> string
                Country = dict.["country"] :?> string
        }), []

let readFutPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "positionsAllowed",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "positionsAllowed" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLFutPf(
        {
                PfId = dict.["pfId"] :?> string
                PfCode = dict.["pfCode"] :?> string
                Name = dict.["name"] :?> string
                Currency = dict.["currency"] :?> string
                Cvf = dict.["cvf"] :?> float
                PriceDl = dict.["priceDl"] :?> int
                PriceFmt = dict.["priceFmt"] :?> string
                ValueMeth = dict.["valueMeth"] :?> string
                PriceMeth = dict.["priceMeth"] :?> string
                SetlMeth = dict.["setlMeth"] :?> string
                PositionsAllowed = dict.["positionsAllowed"] :?> int
        }), []

let readOopPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "strikeDl",0:>obj; "strikeFmt","":>obj; "cab",0.0:>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "priceModel","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "strikeDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "strikeFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "cab" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceModel" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLOopPf(
        {
                PfId = dict.["pfId"] :?> string
                PfCode = dict.["pfCode"] :?> string
                Name = dict.["name"] :?> string
                Currency = dict.["currency"] :?> string
                Cvf = dict.["cvf"] :?> float
                PriceDl = dict.["priceDl"] :?> int
                PriceFmt = dict.["priceFmt"] :?> string
                StrikeDl = dict.["strikeDl"] :?> int
                StrikeFmt = dict.["strikeFmt"] :?> string
                Cab = dict.["cab"] :?> float
                ValueMeth = dict.["valueMeth"] :?> string
                PriceMeth = dict.["priceMeth"] :?> string
                SetlMeth = dict.["setlMeth"] :?> string
                PriceModel = dict.["priceModel"] :?> string
        }), []

let readOofPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "exercise","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "strikeDl",0:>obj; "strikeFmt","":>obj; "cab",0.0:>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "priceModel","":>obj; "isVariableTick",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "exercise" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "strikeDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "strikeFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "cab" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceModel" as name -> dict.[name] <- readAsString reader ; read ()
        | "isVariableTick" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLOofPf(
        {
                PfId = dict.["pfId"] :?> string
                PfCode = dict.["pfCode"] :?> string
                Name = dict.["name"] :?> string
                Exercise = dict.["exercise"] :?> string
                Currency = dict.["currency"] :?> string
                Cvf = dict.["cvf"] :?> float
                PriceDl = dict.["priceDl"] :?> int
                PriceFmt = dict.["priceFmt"] :?> string
                StrikeDl = dict.["strikeDl"] :?> int
                StrikeFmt = dict.["strikeFmt"] :?> string
                Cab = dict.["cab"] :?> float
                ValueMeth = dict.["valueMeth"] :?> string
                PriceMeth = dict.["priceMeth"] :?> string
                SetlMeth = dict.["setlMeth"] :?> string
                PriceModel = dict.["priceModel"] :?> string
                IsVariableTick = dict.["isVariableTick"] :?> int
        }), []

let readOoePf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "exercise","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "strikeDl",0:>obj; "strikeFmt","":>obj; "cab",0.0:>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "priceModel","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "exercise" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "strikeDl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "strikeFmt" as name -> dict.[name] <- readAsString reader ; read ()
        | "cab" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "priceModel" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLOoePf(
        {
                PfId = dict.["pfId"] :?> string
                PfCode = dict.["pfCode"] :?> string
                Name = dict.["name"] :?> string
                Exercise = dict.["exercise"] :?> string
                Currency = dict.["currency"] :?> string
                Cvf = dict.["cvf"] :?> float
                PriceDl = dict.["priceDl"] :?> int
                PriceFmt = dict.["priceFmt"] :?> string
                StrikeDl = dict.["strikeDl"] :?> int
                StrikeFmt = dict.["strikeFmt"] :?> string
                Cab = dict.["cab"] :?> float
                ValueMeth = dict.["valueMeth"] :?> string
                PriceMeth = dict.["priceMeth"] :?> string
                SetlMeth = dict.["setlMeth"] :?> string
                PriceModel = dict.["priceModel"] :?> string
        }), []

let readPfLink (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["exch","":>obj; "pfId",0:>obj; "pfCode","":>obj; "pfType","":>obj; "sc",0.0:>obj; "cmbMeth","":>obj; "applyBasisRisk",0:>obj; "oopDeltaMeth","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "exch" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "pfType" as name -> dict.[name] <- readAsString reader ; read ()
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "cmbMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "applyBasisRisk" as name -> dict.[name] <- readAsInt reader ; read ()
        | "oopDeltaMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPfLink(
        {
                Exch = dict.["exch"] :?> string
                PfId = dict.["pfId"] :?> int
                PfCode = dict.["pfCode"] :?> string
                PfType = dict.["pfType"] :?> string
                Sc = dict.["sc"] :?> float
                CmbMeth = dict.["cmbMeth"] :?> string
                ApplyBasisRisk = dict.["applyBasisRisk"] :?> int
                OopDeltaMeth = dict.["oopDeltaMeth"] :?> string
        }), []

let readDSpread (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["spread",0:>obj; "chargeMeth","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "spread" as name -> dict.[name] <- readAsInt reader ; read ()
        | "chargeMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLDSpread(
        {
                Spread = dict.["spread"] :?> int
                ChargeMeth = dict.["chargeMeth"] :?> string
        }), []

let readHSpread (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["spread",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "spread" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLHSpread(
        {
                Spread = dict.["spread"] :?> int
        }), []

let readSpotRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "pe",0:>obj; "sprd",0.0:>obj; "outr",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "sprd" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "outr" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLSpotRate(
        {
                R = dict.["r"] :?> int
                Pe = dict.["pe"] :?> int
                Sprd = dict.["sprd"] :?> float
                Outr = dict.["outr"] :?> float
        }), []

let readCurConv (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["fromCur","":>obj; "toCur","":>obj; "factor",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "fromCur" as name -> dict.[name] <- readAsString reader ; read ()
        | "toCur" as name -> dict.[name] <- readAsString reader ; read ()
        | "factor" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLCurConv(
        {
                FromCur = dict.["fromCur"] :?> string
                ToCur = dict.["toCur"] :?> string
                Factor = dict.["factor"] :?> float
        }), []

let readPbRateDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "isCust",0:>obj; "acctType","":>obj; "isM",0:>obj; "pbc","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | "isCust" as name -> dict.[name] <- readAsInt reader ; read ()
        | "acctType" as name -> dict.[name] <- readAsString reader ; read ()
        | "isM" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pbc" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPbRateDef(
        {
                R = dict.["r"] :?> int
                IsCust = dict.["isCust"] :?> int
                AcctType = dict.["acctType"] :?> string
                IsM = dict.["isM"] :?> int
                Pbc = dict.["pbc"] :?> string
        }), []

let readPointDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPointDef(
        {
                R = dict.["r"] :?> int
        }), []

let readExchange (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["exch","":>obj; "name","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "exch" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLExchange(
        {
                Exch = dict.["exch"] :?> string
                Name = dict.["name"] :?> string
        }), []

let readCcDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cc","":>obj; "name","":>obj; "currency","":>obj; "riskExponent",0:>obj; "capAnov",0:>obj; "procMeth","":>obj; "wfprMeth","":>obj; "spotMeth","":>obj; "somMeth","":>obj; "cmbMeth","":>obj; "marginMeth","":>obj; "factorCurveSetId",0:>obj; "factorScenarioSetId",0:>obj; "interCurScan",0:>obj; "limitArraysTo16Points",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cc" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "riskExponent" as name -> dict.[name] <- readAsInt reader ; read ()
        | "capAnov" as name -> dict.[name] <- readAsInt reader ; read ()
        | "procMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "wfprMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "spotMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "somMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "cmbMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "marginMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "factorCurveSetId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "factorScenarioSetId" as name -> dict.[name] <- readAsInt reader ; read ()
        | "interCurScan" as name -> dict.[name] <- readAsInt reader ; read ()
        | "limitArraysTo16Points" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLCcDef(
        {
                Cc = dict.["cc"] :?> string
                Name = dict.["name"] :?> string
                Currency = dict.["currency"] :?> string
                RiskExponent = dict.["riskExponent"] :?> int
                CapAnov = dict.["capAnov"] :?> int
                ProcMeth = dict.["procMeth"] :?> string
                WfprMeth = dict.["wfprMeth"] :?> string
                SpotMeth = dict.["spotMeth"] :?> string
                SomMeth = dict.["somMeth"] :?> string
                CmbMeth = dict.["cmbMeth"] :?> string
                MarginMeth = dict.["marginMeth"] :?> string
                FactorCurveSetId = dict.["factorCurveSetId"] :?> int
                FactorScenarioSetId = dict.["factorScenarioSetId"] :?> int
                InterCurScan = dict.["interCurScan"] :?> int
                LimitArraysTo16Points = dict.["limitArraysTo16Points"] :?> int
        }), []

let readCurrencyDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["currency","":>obj; "symbol","":>obj; "name","":>obj; "decimalPos",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "symbol" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "decimalPos" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLCurrencyDef(
        {
                Currency = dict.["currency"] :?> string
                Symbol = dict.["symbol"] :?> string
                Name = dict.["name"] :?> string
                DecimalPos = dict.["decimalPos"] :?> int
        }), []

let readAcctTypeDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["isCust",0:>obj; "acctType","":>obj; "name","":>obj; "isNetMargin",0:>obj; "priority",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "isCust" as name -> dict.[name] <- readAsInt reader ; read ()
        | "acctType" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "isNetMargin" as name -> dict.[name] <- readAsInt reader ; read ()
        | "priority" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLAcctTypeDef(
        {
                IsCust = dict.["isCust"] :?> int
                AcctType = dict.["acctType"] :?> string
                Name = dict.["name"] :?> string
                IsNetMargin = dict.["isNetMargin"] :?> int
                Priority = dict.["priority"] :?> int
        }), []

let readAcctSubTypeDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["acctSubTypeCode","":>obj; "dataType","":>obj; "description","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "acctSubTypeCode" as name -> dict.[name] <- readAsString reader ; read ()
        | "dataType" as name -> dict.[name] <- readAsString reader ; read ()
        | "description" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLAcctSubTypeDef(
        {
                AcctSubTypeCode = dict.["acctSubTypeCode"] :?> string
                DataType = dict.["dataType"] :?> string
                Description = dict.["description"] :?> string
        }), []

let readGroupTypeDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["id",0:>obj; "name","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "id" as name -> dict.[name] <- readAsInt reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLGroupTypeDef(
        {
                Id = dict.["id"] :?> int
                Name = dict.["name"] :?> string
        }), []

let readGroupDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["id",0:>obj; "aval","":>obj; "description","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "id" as name -> dict.[name] <- readAsInt reader ; read ()
        | "aval" as name -> dict.[name] <- readAsString reader ; read ()
        | "description" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLGroupDef(
        {
                Id = dict.["id"] :?> int
                Aval = dict.["aval"] :?> string
                Description = dict.["description"] :?> string
        }), []

let readClearingOrg (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["ec","":>obj; "name","":>obj; "isContractScale",0:>obj; "isNetMargin",0:>obj; "finalizeMeth","":>obj; "oopDeltaMeth","":>obj; "capAnov",0:>obj; "lookAheadYears",0.0:>obj; "daysPerYear",0:>obj; "limitSubAccountOffset",0:>obj; "lookAheadDays",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "ec" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "isContractScale" as name -> dict.[name] <- readAsInt reader ; read ()
        | "isNetMargin" as name -> dict.[name] <- readAsInt reader ; read ()
        | "finalizeMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "oopDeltaMeth" as name -> dict.[name] <- readAsString reader ; read ()
        | "capAnov" as name -> dict.[name] <- readAsInt reader ; read ()
        | "lookAheadYears" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "daysPerYear" as name -> dict.[name] <- readAsInt reader ; read ()
        | "limitSubAccountOffset" as name -> dict.[name] <- readAsInt reader ; read ()
        | "lookAheadDays" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLClearingOrg(
        {
                Ec = dict.["ec"] :?> string
                Name = dict.["name"] :?> string
                IsContractScale = dict.["isContractScale"] :?> int
                IsNetMargin = dict.["isNetMargin"] :?> int
                FinalizeMeth = dict.["finalizeMeth"] :?> string
                OopDeltaMeth = dict.["oopDeltaMeth"] :?> string
                CapAnov = dict.["capAnov"] :?> int
                LookAheadYears = dict.["lookAheadYears"] :?> float
                DaysPerYear = dict.["daysPerYear"] :?> int
                LimitSubAccountOffset = dict.["limitSubAccountOffset"] :?> int
                LookAheadDays = dict.["lookAheadDays"] :?> int
        }), []

let readDefinitions (reader:System.Xml.XmlReader) : nodeType * tree list =

    let rec read currencyDef acctTypeDef acctSubTypeDef groupTypeDef groupDef =
        match reader.Name with
        | "currencyDef" as name -> read (Node (readCurrencyDef reader) :: currencyDef) acctTypeDef acctSubTypeDef groupTypeDef groupDef 
        | "acctTypeDef" as name -> read currencyDef (Node (readAcctTypeDef reader) :: acctTypeDef) acctSubTypeDef groupTypeDef groupDef 
        | "acctSubTypeDef" as name -> read currencyDef acctTypeDef (Node (readAcctSubTypeDef reader) :: acctSubTypeDef) groupTypeDef groupDef 
        | "groupTypeDef" as name -> read currencyDef acctTypeDef acctSubTypeDef (Node (readGroupTypeDef reader) :: groupTypeDef) groupDef 
        | "groupDef" as name -> read currencyDef acctTypeDef acctSubTypeDef groupTypeDef (Node (readGroupDef reader) :: groupDef) 
        | _ -> currencyDef, acctTypeDef, acctSubTypeDef, groupTypeDef, groupDef
    let currencyDef, acctTypeDef, acctSubTypeDef, groupTypeDef, groupDef = read [] [] [] [] []

    SpanXMLDefinition (new SpanXMLDefinition()), (currencyDef @ acctTypeDef @ acctSubTypeDef @ groupTypeDef @ groupDef)

let readPointInTime (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["date",0:>obj; "isSetl",0:>obj; "setlQualifier","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "date" as name -> dict.[name] <- readAsInt reader ; read ()
        | "isSetl" as name -> dict.[name] <- readAsInt reader ; read ()
        | "setlQualifier" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    read ()
    SpanXMLPointInTime(
        {
                Date = dict.["date"] :?> int
                IsSetl = dict.["isSetl"] :?> int
                SetlQualifier = dict.["setlQualifier"] :?> string
        }), []

let readLevel2 (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["fileFormat","":>obj; "created",0L:>obj; ] |> toDict

    let rec read definitions pointInTime =
        match reader.Name with
        | "fileFormat" as name -> dict.[name] <- readAsString reader ; read definitions pointInTime
        | "created" as name -> dict.[name] <- readAsInt64 reader ; read definitions pointInTime
        | "definitions" as name -> read (Node (readDefinitions reader) :: definitions) pointInTime
        | "pointInTime" as name -> read definitions (Node (readPointInTime reader) :: pointInTime)
        | _ -> definitions, pointInTime
    let definitions, pointInTime = read [] []
    SpanXMLLevel2(
        {
                FileFormat = dict.["fileFormat"] :?> string
                Created = dict.["created"] :?> int64
        }), (definitions @ pointInTime)


let readSpanFile (reader:System.Xml.XmlReader) lst : SpanXMLLevel2 list = lst

 // Level 1
let readXML (reader:System.Xml.XmlReader) =
    Node (SpanXMLTopLevel, (if reader.Name = "spanFile" && reader.Read () then readSpanFile reader [] else []))



