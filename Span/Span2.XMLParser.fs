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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    let a = read []
    reader.ReadEndElement()
    SpanXMLRa(
        {
                R = dict.["r"] :?> int
                A = a
                D = dict.["d"] :?> float
        }), []

let readDivRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["val",0.0:>obj; ] |> toDict

    let rec read div =
        match reader.Name with
        | "val" as name -> dict.[name] <- readAsFloat reader ; read div
        | "div" as name -> read (Node (readDiv reader) :: div)
        | _ -> div
    reader.ReadStartElement()
    let div = read []
    reader.ReadEndElement()
    SpanXMLDivRate(
        {
                Val = dict.["val"] :?> float
        }), div

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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLIntrRate(
        {
                Val = dict.["val"] :?> float
                Rl = dict.["rl"] :?> int
                Cpm = dict.["cpm"] :?> int
                Exm = dict.["exm"] :?> int
        }), []

let readOpt (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "o","":>obj; "k",0.0:>obj; "p",0.0:>obj; "pq",0:>obj; "d",0.0:>obj; "v",0.0:>obj; "val",0.0:>obj; "cvf",0.0:>obj; "svf",0.0:>obj; ] |> toDict

    let rec read ra =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ra
        | "o" as name -> dict.[name] <- readAsString reader ; read ra
        | "k" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "p" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "pq" as name -> dict.[name] <- readAsInt reader ; read ra
        | "d" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "svf" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "ra" as name -> read (Node (readRa reader) :: ra)
        | _ -> ra
    reader.ReadStartElement()
    let ra = read []
    reader.ReadEndElement()
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
        }), ra

let readRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "val",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLVolScanDef(
        {
                Mult = dict.["mult"] :?> float
                Numerator = dict.["numerator"] :?> float
                Denominator = dict.["denominator"] :?> float
        }), []

let readPhy (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "pe","":>obj; "p",0.0:>obj; "d",0.0:>obj; "v",0.0:>obj; "cvf",0.0:>obj; "val",0.0:>obj; "sc",0.0:>obj; ] |> toDict

    let rec read ra =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read ra
        | "pe" as name -> dict.[name] <- readAsString reader ; read ra
        | "p" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "d" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "v" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "val" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read ra
        | "ra" as name -> read (Node (readRa reader) :: ra)
        | _ -> ra
    reader.ReadStartElement()
    let ra = read []
    reader.ReadEndElement()
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
        }), ra

let readGroup (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["id",0:>obj; "aVal","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "id" as name -> dict.[name] <- readAsInt reader ; read ()
        | "aVal" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLGroup(
        {
                Id = dict.["id"] :?> int
                Aval = dict.["aVal"] :?> string
        }), []

let readEquity (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cId",0:>obj; "isin","":>obj; "pe","":>obj; "p",0.0:>obj; "d",0.0:>obj; "v",0.0:>obj; "cvf",0.0:>obj; "val",0.0:>obj; "sc",0.0:>obj; "desc","":>obj; "type","":>obj; "subType","":>obj; ] |> toDict

    let rec read divRate ra =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read divRate ra
        | "isin" as name -> dict.[name] <- readAsString reader ; read divRate ra
        | "pe" as name -> dict.[name] <- readAsString reader ; read divRate ra
        | "p" as name -> dict.[name] <- readAsFloat reader ; read divRate ra
        | "d" as name -> dict.[name] <- readAsFloat reader ; read divRate ra
        | "v" as name -> dict.[name] <- readAsFloat reader ; read divRate ra
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read divRate ra
        | "val" as name -> dict.[name] <- readAsFloat reader ; read divRate ra
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read divRate ra
        | "desc" as name -> dict.[name] <- readAsString reader ; read divRate ra
        | "type" as name -> dict.[name] <- readAsString reader ; read divRate ra
        | "subType" as name -> dict.[name] <- readAsString reader ; read divRate ra
        | "divRate" as name -> read (Node (readDivRate reader) :: divRate) ra
        | "ra" as name -> read divRate (Node (readRa reader) :: ra)
        | _ -> divRate, ra
    reader.ReadStartElement()
    let divRate, ra = read [] []
    reader.ReadEndElement()
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
        }), (divRate @ ra)

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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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

    let rec read undC ra scanRate =
        match reader.Name with
        | "cId" as name -> dict.[name] <- readAsInt reader ; read undC ra scanRate
        | "pe" as name -> dict.[name] <- readAsInt reader ; read undC ra scanRate
        | "p" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "d" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "v" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "val" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "setlDate" as name -> dict.[name] <- readAsInt reader ; read undC ra scanRate
        | "t" as name -> dict.[name] <- readAsFloat reader ; read undC ra scanRate
        | "undC" as name -> read (Node (readUndC reader) :: undC) ra scanRate
        | "ra" as name -> read undC (Node (readRa reader) :: ra) scanRate
        | "scanRate" as name -> read undC ra (Node (readScanRate reader) :: scanRate)
        | _ -> undC, ra, scanRate
    reader.ReadStartElement()
    let undC, ra, scanRate = read [] [] []
    reader.ReadEndElement()
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
        }), (undC @ ra @ scanRate)

let readSeries (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pe",0:>obj; "v",0.0:>obj; "volSrc","":>obj; "setlDate",0:>obj; "t",0.0:>obj; "cvf",0.0:>obj; "svf",0.0:>obj; "sc",0.0:>obj; ] |> toDict

    let rec read undC intrRate divRate scanRate opt =
        match reader.Name with
        | "pe" as name -> dict.[name] <- readAsInt reader ; read undC intrRate divRate scanRate opt
        | "v" as name -> dict.[name] <- readAsFloat reader ; read undC intrRate divRate scanRate opt
        | "volSrc" as name -> dict.[name] <- readAsString reader ; read undC intrRate divRate scanRate opt
        | "setlDate" as name -> dict.[name] <- readAsInt reader ; read undC intrRate divRate scanRate opt
        | "t" as name -> dict.[name] <- readAsFloat reader ; read undC intrRate divRate scanRate opt
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read undC intrRate divRate scanRate opt
        | "svf" as name -> dict.[name] <- readAsFloat reader ; read undC intrRate divRate scanRate opt
        | "sc" as name -> dict.[name] <- readAsFloat reader ; read undC intrRate divRate scanRate opt
        | "undC" as name -> read (Node (readUndC reader) :: undC) intrRate divRate scanRate opt
        | "intrRate" as name -> read undC (Node (readIntrRate reader) :: intrRate) divRate scanRate opt
        | "divRate" as name -> read undC intrRate (Node (readDivRate reader) :: divRate) scanRate opt
        | "scanRate" as name -> read undC intrRate divRate (Node (readScanRate reader) :: scanRate) opt
        | "opt" as name -> read undC intrRate divRate scanRate (Node (readOpt reader) :: opt)
        | _ -> undC, intrRate, divRate, scanRate, opt
    reader.ReadStartElement()
    let undC, intrRate, divRate, scanRate, opt = read [] [] [] [] []
    reader.ReadEndElement()
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
        }), (undC @ intrRate @ divRate @ scanRate @ opt)

let readTier (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["tn",0:>obj; "ePe",0:>obj; "sPe",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "tn" as name -> dict.[name] <- readAsInt reader ; read ()
        | "ePe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "sPe" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLTier(
        {
                Tn = dict.["tn"] :?> int
                EPe = dict.["ePe"] :?> int
                SPe = dict.["sPe"] :?> int
        }), []

let readTierWithRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["tn",0:>obj; ] |> toDict

    let rec read rate =
        match reader.Name with
        | "tn" as name -> dict.[name] <- readAsInt reader ; read rate
        | "rate" as name -> read (Node (readRate reader) :: rate)
        | _ -> rate
    reader.ReadStartElement()
    let rate = read []
    reader.ReadEndElement()
    SpanXMLTierWithRate(
        {
                Tn = dict.["tn"] :?> int
        }), rate

let readTierWithScanRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["tn",0:>obj; "ePe",0:>obj; "sPe",0:>obj; ] |> toDict

    let rec read scanRate =
        match reader.Name with
        | "tn" as name -> dict.[name] <- readAsInt reader ; read scanRate
        | "ePe" as name -> dict.[name] <- readAsInt reader ; read scanRate
        | "sPe" as name -> dict.[name] <- readAsInt reader ; read scanRate
        | "scanRate" as name -> read (Node (readScanRate reader) :: scanRate)
        | _ -> scanRate
    reader.ReadStartElement()
    let scanRate = read []
    reader.ReadEndElement()
    SpanXMLTierWithScanRate(
        {
                Tn = dict.["tn"] :?> int
                EPe = dict.["ePe"] :?> int
                SPe = dict.["sPe"] :?> int
        }), scanRate

let readTLeg (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cc","":>obj; "tn",0:>obj; "rs","":>obj; "i",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "cc" as name -> dict.[name] <- readAsString reader ; read ()
        | "tn" as name -> dict.[name] <- readAsInt reader ; read ()
        | "rs" as name -> dict.[name] <- readAsString reader ; read ()
        | "i" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLSLeg(
        {
                Cc = dict.["cc"] :?> string
                IsTarget = dict.["isTarget"] :?> int
                IsRequired = dict.["isRequired"] :?> int
        }), []

let readScanPointDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["point",0:>obj; "weight",0.0:>obj; "pairedPoint",0:>obj; ] |> toDict

    let rec read priceScanDef volScanDef =
        match reader.Name with
        | "point" as name -> dict.[name] <- readAsInt reader ; read priceScanDef volScanDef
        | "weight" as name -> dict.[name] <- readAsFloat reader ; read priceScanDef volScanDef
        | "pairedPoint" as name -> dict.[name] <- readAsInt reader ; read priceScanDef volScanDef
        | "priceScanDef" as name -> read (Node (readPriceScanDef reader) :: priceScanDef) volScanDef
        | "volScanDef" as name -> read priceScanDef (Node (readVolScanDef reader) :: volScanDef)
        | _ -> priceScanDef, volScanDef
    reader.ReadStartElement()
    let priceScanDef, volScanDef = read [] []
    reader.ReadEndElement()
    SpanXMLScanPointDef(
        {
                Point = dict.["point"] :?> int
                Weight = dict.["weight"] :?> float
                PairedPoint = dict.["pairedPoint"] :?> int
        }), (priceScanDef @ volScanDef)

let readDeltaPointDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["point",0:>obj; "weight",0.0:>obj; ] |> toDict

    let rec read priceScanDef volScanDef =
        match reader.Name with
        | "point" as name -> dict.[name] <- readAsInt reader ; read priceScanDef volScanDef
        | "weight" as name -> dict.[name] <- readAsFloat reader ; read priceScanDef volScanDef
        | "priceScanDef" as name -> read (Node (readPriceScanDef reader) :: priceScanDef) volScanDef
        | "volScanDef" as name -> read priceScanDef (Node (readVolScanDef reader) :: volScanDef)
        | _ -> priceScanDef, volScanDef
    reader.ReadStartElement()
    let priceScanDef, volScanDef = read [] []
    reader.ReadEndElement()
    SpanXMLDeltaPointDef(
        {
                Point = dict.["point"] :?> int
                Weight = dict.["weight"] :?> float
        }), (priceScanDef @ volScanDef)

let readPhyPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId",0:>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "positionsAllowed",0:>obj; ] |> toDict

    let rec read phy =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsInt reader ; read phy
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read phy
        | "name" as name -> dict.[name] <- readAsString reader ; read phy
        | "currency" as name -> dict.[name] <- readAsString reader ; read phy
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read phy
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read phy
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read phy
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read phy
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read phy
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read phy
        | "positionsAllowed" as name -> dict.[name] <- readAsInt reader ; read phy
        | "phy" as name -> read (Node (readPhy reader) :: phy)
        | _ -> phy
    reader.ReadStartElement()
    let phy = read []
    reader.ReadEndElement()
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
        }), phy

let readEquityPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "country","":>obj; ] |> toDict

    let rec read group equity =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read group equity
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read group equity
        | "name" as name -> dict.[name] <- readAsString reader ; read group equity
        | "currency" as name -> dict.[name] <- readAsString reader ; read group equity
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read group equity
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read group equity
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read group equity
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read group equity
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read group equity
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read group equity
        | "country" as name -> dict.[name] <- readAsString reader ; read group equity
        | "group" as name -> read (Node (readGroup reader) :: group) equity
        | "equity" as name -> read group (Node (readEquity reader) :: equity)
        | _ -> group, equity
    reader.ReadStartElement()
    let group, equity = read [] []
    reader.ReadEndElement()
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
        }), (group @ equity)

let readFutPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "positionsAllowed",0:>obj; ] |> toDict

    let rec read group undPf fut =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "name" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "currency" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read group undPf fut
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read group undPf fut
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read group undPf fut
        | "positionsAllowed" as name -> dict.[name] <- readAsInt reader ; read group undPf fut
        | "group" as name -> read (Node (readGroup reader) :: group) undPf fut
        | "undPf" as name -> read group (Node (readUndPf reader) :: undPf) fut
        | "fut" as name -> read group undPf (Node (readFut reader) :: fut)
        | _ -> group, undPf, fut
    reader.ReadStartElement()
    let group, undPf, fut = read [] [] []
    reader.ReadEndElement()
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
        }), (group @ undPf @ fut)

let readOopPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "strikeDl",0:>obj; "strikeFmt","":>obj; "cab",0.0:>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "priceModel","":>obj; ] |> toDict

    let rec read group undPf =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "name" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "exercise" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "currency" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read group undPf
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read group undPf
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "strikeDl" as name -> dict.[name] <- readAsInt reader ; read group undPf
        | "strikeFmt" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "cab" as name -> dict.[name] <- readAsFloat reader ; read group undPf
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "priceModel" as name -> dict.[name] <- readAsString reader ; read group undPf
        | "group" as name -> read (Node (readGroup reader) :: group) undPf
        | "undPf" as name -> read group (Node (readUndPf reader) :: undPf)
        | _ -> group, undPf
    reader.ReadStartElement()
    let group, undPf = read [] []
    reader.ReadEndElement()
    SpanXMLOopPf(
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
        }), (group @ undPf)

let readOofPf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "exercise","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "strikeDl",0:>obj; "strikeFmt","":>obj; "cab",0.0:>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "priceModel","":>obj; "isVariableTick",0:>obj; ] |> toDict

    let rec read group undPf series =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "name" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "exercise" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "currency" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read group undPf series
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read group undPf series
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "strikeDl" as name -> dict.[name] <- readAsInt reader ; read group undPf series
        | "strikeFmt" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "cab" as name -> dict.[name] <- readAsFloat reader ; read group undPf series
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "priceModel" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "isVariableTick" as name -> dict.[name] <- readAsInt reader ; read group undPf series
        | "group" as name -> read (Node (readGroup reader) :: group) undPf series
        | "undPf" as name -> read group (Node (readUndPf reader) :: undPf) series
        | "series" as name -> read group undPf (Node (readSeries reader) :: series)
        | _ -> group, undPf, series
    reader.ReadStartElement()
    let group, undPf, series = read [] [] []
    reader.ReadEndElement()
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
        }), (group @ undPf @ series)

let readOoePf (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["pfId","":>obj; "pfCode","":>obj; "name","":>obj; "exercise","":>obj; "currency","":>obj; "cvf",0.0:>obj; "priceDl",0:>obj; "priceFmt","":>obj; "strikeDl",0:>obj; "strikeFmt","":>obj; "cab",0.0:>obj; "valueMeth","":>obj; "priceMeth","":>obj; "setlMeth","":>obj; "priceModel","":>obj; ] |> toDict

    let rec read group undPf series =
        match reader.Name with
        | "pfId" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "pfCode" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "name" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "exercise" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "currency" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "cvf" as name -> dict.[name] <- readAsFloat reader ; read group undPf series
        | "priceDl" as name -> dict.[name] <- readAsInt reader ; read group undPf series
        | "priceFmt" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "strikeDl" as name -> dict.[name] <- readAsInt reader ; read group undPf series
        | "strikeFmt" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "cab" as name -> dict.[name] <- readAsFloat reader ; read group undPf series
        | "valueMeth" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "priceMeth" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "setlMeth" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "priceModel" as name -> dict.[name] <- readAsString reader ; read group undPf series
        | "group" as name -> read (Node (readGroup reader) :: group) undPf series
        | "undPf" as name -> read group (Node (readUndPf reader) :: undPf) series
        | "series" as name -> read group undPf (Node (readSeries reader) :: series)
        | _ -> group, undPf, series
    reader.ReadStartElement()
    let group, undPf, series = read [] [] []
    reader.ReadEndElement()
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
        }), (group @ undPf @ series)

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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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

let readIntraTiers (reader:System.Xml.XmlReader) : nodeType * tree list =
    let rec read tier =
        match reader.Name with
        | "tier" as name -> read (Node (readTier reader) :: tier)
        | _ -> tier
    reader.ReadStartElement()
    let tier = read []
    reader.ReadEndElement()
    SpanXMLIntraTiers( new SpanXMLIntraTiers()), tier

let readInterTiers (reader:System.Xml.XmlReader) : nodeType * tree list =
    let rec read tier =
        match reader.Name with
        | "tier" as name -> read (Node (readTier reader) :: tier)
        | _ -> tier
    reader.ReadStartElement()
    let tier = read []
    reader.ReadEndElement()
    SpanXMLInterTiers( new SpanXMLInterTiers()), tier

let readSomTiers (reader:System.Xml.XmlReader) : nodeType * tree list =
    let rec read tier =
        match reader.Name with
        | "tier" as name -> read (Node (readTierWithRate reader) :: tier)
        | _ -> tier
    reader.ReadStartElement()
    let tier = read []
    reader.ReadEndElement()
    SpanXMLSomTiers( new SpanXMLSomTiers()), tier

let readRateTiers (reader:System.Xml.XmlReader) : nodeType * tree list =
    let rec read tier =
        match reader.Name with
        | "tier" as name -> read (Node (readTierWithScanRate reader) :: tier)
        | _ -> tier
    reader.ReadStartElement()
    let tier = read []
    reader.ReadEndElement()
    SpanXMLRateTiers( new SpanXMLRateTiers()), tier

let readDSpread (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["spread",0:>obj; "chargeMeth","":>obj; ] |> toDict

    let rec read rate tLeg pLeg =
        match reader.Name with
        | "spread" as name -> dict.[name] <- readAsInt reader ; read rate tLeg pLeg
        | "chargeMeth" as name -> dict.[name] <- readAsString reader ; read rate tLeg pLeg
        | "rate" as name -> read (Node (readRate reader) :: rate) tLeg pLeg
        | "tLeg" as name -> read rate (Node (readTLeg reader) :: tLeg) pLeg
        | "pLeg" as name -> read rate tLeg (Node (readPLeg reader) :: pLeg)
        | _ -> rate, tLeg, pLeg
    reader.ReadStartElement()
    let rate, tLeg, pLeg = read [] [] []
    reader.ReadEndElement()
    SpanXMLDSpread(
        {
                Spread = dict.["spread"] :?> int;
                ChargeMeth = dict.["chargeMeth"] :?> string
        }), (rate @ tLeg @ pLeg)

let readHSpread (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["spread",0:>obj; ] |> toDict

    let rec read rate tLeg sLeg =
        match reader.Name with
        | "spread" as name -> dict.[name] <- readAsInt reader ; read rate tLeg sLeg
        | "rate" as name -> read (Node (readRate reader) :: rate) tLeg sLeg
        | "tLeg" as name -> read rate (Node (readTLeg reader) :: tLeg) sLeg
        | "sLeg" as name -> read rate tLeg (Node (readSLeg reader) :: sLeg)
        | _ -> rate, tLeg, sLeg
    reader.ReadStartElement()
    let rate, tLeg, sLeg = read [] [] []
    reader.ReadEndElement()
    SpanXMLHSpread(
        {
                Spread = dict.["spread"] :?> int
        }), (rate @ tLeg @ sLeg)

let readSpotRate (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["r",0:>obj; "pe",0:>obj; "sprd",0.0:>obj; "outr",0.0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read ()
        | "pe" as name -> dict.[name] <- readAsInt reader ; read ()
        | "sprd" as name -> dict.[name] <- readAsFloat reader ; read ()
        | "outr" as name -> dict.[name] <- readAsFloat reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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

    let rec read scanPointDef deltaPointDef =
        match reader.Name with
        | "r" as name -> dict.[name] <- readAsInt reader ; read scanPointDef deltaPointDef
        | "scanPointDef" as name -> read (Node (readScanPointDef reader) :: scanPointDef) deltaPointDef
        | "deltaPointDef" as name -> read scanPointDef (Node (readDeltaPointDef reader) :: deltaPointDef)
        | _ -> scanPointDef, deltaPointDef
    reader.ReadStartElement()
    let scanPointDef, deltaPointDef = read [] []
    reader.ReadEndElement()
    SpanXMLPointDef(
        {
                R = dict.["r"] :?> int
        }), (scanPointDef @ deltaPointDef)

let readExchange (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["exch","":>obj; "name","":>obj; ] |> toDict

    let rec read phyPf equityPf futPf ooePf oofPf oopPf =
        match reader.Name with
        | "exch" as name -> dict.[name] <- readAsString reader ; read phyPf equityPf futPf ooePf oofPf oopPf
        | "name" as name -> dict.[name] <- readAsString reader ; read phyPf equityPf futPf ooePf oofPf oopPf
        | "phyPf" as name -> read (Node (readPhyPf reader) :: phyPf) equityPf futPf ooePf oofPf oopPf
        | "equityPf" as name -> read phyPf (Node (readEquityPf reader) :: equityPf) futPf ooePf oofPf oopPf
        | "futPf" as name -> read phyPf equityPf (Node (readFutPf reader) :: futPf) ooePf oofPf oopPf
        | "ooePf" as name -> read phyPf equityPf futPf (Node (readOoePf reader) :: ooePf) oofPf oopPf
        | "oofPf" as name -> read phyPf equityPf futPf ooePf (Node (readOofPf reader) :: oofPf) oopPf
        | "oopPf" as name -> read phyPf equityPf futPf ooePf oofPf (Node (readOopPf reader) :: oopPf)
        | _ -> phyPf, equityPf, futPf, ooePf, oofPf, oopPf
    reader.ReadStartElement()
    let phyPf, equityPf, futPf, ooePf, oofPf, oopPf = read [] [] [] [] [] []
    reader.ReadEndElement()
    SpanXMLExchange(
        {
                Exch = dict.["exch"] :?> string
                Name = dict.["name"] :?> string
        }), (phyPf @ equityPf @ futPf @ ooePf @ oofPf @ oopPf)

let readCcDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["cc","":>obj; "name","":>obj; "currency","":>obj; "riskExponent",0:>obj; "capAnov",0:>obj; "procMeth","":>obj; "wfprMeth","":>obj; "spotMeth","":>obj; "somMeth","":>obj; "cmbMeth","":>obj; "marginMeth","":>obj; "factorCurveSetId",0:>obj; "factorScenarioSetId",0:>obj; "interCurScan",0:>obj; "limitArraysTo16Points",0:>obj; ] |> toDict

    let rec read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group =
        match reader.Name with
        | "cc" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "name" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "currency" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "riskExponent" as name -> dict.[name] <- readAsInt reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "capAnov" as name -> dict.[name] <- readAsInt reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "procMeth" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "wfprMeth" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "spotMeth" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "somMeth" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "cmbMeth" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "marginMeth" as name -> dict.[name] <- readAsString reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "factorCurveSetId" as name -> dict.[name] <- readAsInt reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "factorScenarioSetId" as name -> dict.[name] <- readAsInt reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "interCurScan" as name -> dict.[name] <- readAsInt reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "limitArraysTo16Points" as name -> dict.[name] <- readAsInt reader ; read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "spotRate" as name -> read (Node (readSpotRate reader) :: spotRate) pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "pfLink" as name -> read spotRate (Node (readPfLink reader) :: pfLink) intraTiers interTiers somTiers rateTiers intrRate dSpread group
        | "intraTiers" as name -> read spotRate pfLink (Node (readIntraTiers reader) :: intraTiers) interTiers somTiers rateTiers intrRate dSpread group
        | "interTiers" as name -> read spotRate pfLink intraTiers (Node (readInterTiers reader) :: interTiers) somTiers rateTiers intrRate dSpread group
        | "somTiers" as name -> read spotRate pfLink intraTiers interTiers (Node (readSomTiers reader) :: somTiers) rateTiers intrRate dSpread group
        | "rateTiers" as name -> read spotRate pfLink intraTiers interTiers somTiers (Node (readRateTiers reader) :: rateTiers) intrRate dSpread group
        | "intrRate" as name -> read spotRate pfLink intraTiers interTiers somTiers rateTiers (Node (readIntrRate reader) :: intrRate) dSpread group
        | "dSpread" as name -> read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate (Node (readDSpread reader) :: dSpread) group
        | "group" as name -> read spotRate pfLink intraTiers interTiers somTiers rateTiers intrRate dSpread (Node (readGroup reader) :: group)
        | _ -> spotRate, pfLink, intraTiers, interTiers, somTiers, rateTiers, intrRate, dSpread, group
    reader.ReadStartElement()
    let spotRate, pfLink, intraTiers, interTiers, somTiers, rateTiers, intrRate, dSpread, group = read [] [] [] [] [] [] [] [] []
    reader.ReadEndElement()
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
        }), (spotRate @ pfLink @ intraTiers @ interTiers @ somTiers @ rateTiers @ intrRate @ dSpread @ group)

let readInterSpreads (reader:System.Xml.XmlReader) : nodeType * tree list =
    let rec read dSpread hSpread =
        match reader.Name with
        | "dSpread" as name -> read (Node (readDSpread reader) :: dSpread) hSpread 
        | "hSpread" as name -> read dSpread (Node (readHSpread reader) :: hSpread)
        | _ -> dSpread, hSpread
    reader.ReadStartElement()
    let dSpread, hSpread = read [] []
    reader.ReadEndElement()
    SpanXMLInterSpreads( new SpanXMLInterSpreads()),(hSpread @ dSpread)

let readCurrencyDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["currency","":>obj; "symbol","":>obj; "name","":>obj; "decimalPos",0:>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "currency" as name -> dict.[name] <- readAsString reader ; read ()
        | "symbol" as name -> dict.[name] <- readAsString reader ; read ()
        | "name" as name -> dict.[name] <- readAsString reader ; read ()
        | "decimalPos" as name -> dict.[name] <- readAsInt reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
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
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLGroupTypeDef(
        {
                Id = dict.["id"] :?> int
                Name = dict.["name"] :?> string
        }), []

let readGroupDef (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["id",0:>obj; "aVal","":>obj; "description","":>obj; ] |> toDict

    let rec read () =
        match reader.Name with
        | "id" as name -> dict.[name] <- readAsInt reader ; read ()
        | "aVal" as name -> dict.[name] <- readAsString reader ; read ()
        | "description" as name -> dict.[name] <- readAsString reader ; read ()
        | _ -> ignore ()
    reader.ReadStartElement()
    read ()
    reader.ReadEndElement()
    SpanXMLGroupDef(
        {
                Id = dict.["id"] :?> int
                Aval = dict.["aVal"] :?> string
                Description = dict.["description"] :?> string
        }), []

let readClearingOrg (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["ec","":>obj; "name","":>obj; "isContractScale",0:>obj; "isNetMargin",0:>obj; "finalizeMeth","":>obj; "oopDeltaMeth","":>obj; "capAnov",0:>obj; "lookAheadYears",0.0:>obj; "daysPerYear",0:>obj; "limitSubAccountOffset",0:>obj; "lookAheadDays",0:>obj; ] |> toDict

    let rec read curConv pbRateDef pointDef exchange ccDef interSpreads =
        match reader.Name with
        | "ec" as name -> dict.[name] <- readAsString reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "name" as name -> dict.[name] <- readAsString reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "isContractScale" as name -> dict.[name] <- readAsInt reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "isNetMargin" as name -> dict.[name] <- readAsInt reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "finalizeMeth" as name -> dict.[name] <- readAsString reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "oopDeltaMeth" as name -> dict.[name] <- readAsString reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "capAnov" as name -> dict.[name] <- readAsInt reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "lookAheadYears" as name -> dict.[name] <- readAsFloat reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "daysPerYear" as name -> dict.[name] <- readAsInt reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "limitSubAccountOffset" as name -> dict.[name] <- readAsInt reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "lookAheadDays" as name -> dict.[name] <- readAsInt reader ; read curConv pbRateDef pointDef exchange ccDef interSpreads
        | "curConv" as name -> read (Node (readCurConv reader) :: curConv) pbRateDef pointDef exchange ccDef interSpreads
        | "pbRateDef" as name -> read curConv (Node (readPbRateDef reader) :: pbRateDef) pointDef exchange ccDef interSpreads
        | "pointDef" as name -> read curConv pbRateDef (Node (readPointDef reader) :: pointDef) exchange ccDef interSpreads
        | "exchange" as name -> read curConv pbRateDef pointDef (Node (readExchange reader) :: exchange) ccDef interSpreads
        | "ccDef" as name -> read curConv pbRateDef pointDef exchange (Node (readCcDef reader) :: ccDef) interSpreads
        | "interSpreads" as name -> read curConv pbRateDef pointDef exchange ccDef (Node (readInterSpreads reader) :: interSpreads)
        | _ -> curConv, pbRateDef, pointDef, exchange, ccDef, interSpreads
    reader.ReadStartElement()
    let curConv, pbRateDef, pointDef, exchange, ccDef, interSpreads = read [] [] [] [] [] []
    reader.ReadEndElement()
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
        }), (curConv @ pbRateDef @ pointDef @ exchange @ ccDef @ interSpreads)

let readDefinitions (reader:System.Xml.XmlReader) : nodeType * tree list =

    let rec read currencyDef acctTypeDef acctSubTypeDef groupTypeDef groupDef =
        match reader.Name with
        | "currencyDef" as name -> read (Node (readCurrencyDef reader) :: currencyDef) acctTypeDef acctSubTypeDef groupTypeDef groupDef 
        | "acctTypeDef" as name -> read currencyDef (Node (readAcctTypeDef reader) :: acctTypeDef) acctSubTypeDef groupTypeDef groupDef 
        | "acctSubTypeDef" as name -> read currencyDef acctTypeDef (Node (readAcctSubTypeDef reader) :: acctSubTypeDef) groupTypeDef groupDef 
        | "groupTypeDef" as name -> read currencyDef acctTypeDef acctSubTypeDef (Node (readGroupTypeDef reader) :: groupTypeDef) groupDef 
        | "groupDef" as name -> read currencyDef acctTypeDef acctSubTypeDef groupTypeDef (Node (readGroupDef reader) :: groupDef) 
        | _ -> currencyDef, acctTypeDef, acctSubTypeDef, groupTypeDef, groupDef
    reader.ReadStartElement()
    let currencyDef, acctTypeDef, acctSubTypeDef, groupTypeDef, groupDef = read [] [] [] [] []
    reader.ReadEndElement()

    SpanXMLDefinition (new SpanXMLDefinition()), (currencyDef @ acctTypeDef @ acctSubTypeDef @ groupTypeDef @ groupDef)

let readPointInTime (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["date",0:>obj; "isSetl",0:>obj; "setlQualifier","":>obj; ] |> toDict

    let rec read clearingOrg =
        match reader.Name with
        | "date" as name -> dict.[name] <- readAsInt reader ; read clearingOrg
        | "isSetl" as name -> dict.[name] <- readAsInt reader ; read clearingOrg
        | "setlQualifier" as name -> dict.[name] <- readAsString reader ; read clearingOrg
        | "clearingOrg" as name -> read (Node (readClearingOrg reader) :: clearingOrg)
        | _ -> clearingOrg
    reader.ReadStartElement()
    let clearingOrg = read []
    reader.ReadEndElement()
    SpanXMLPointInTime(
        {
                Date = dict.["date"] :?> int
                IsSetl = dict.["isSetl"] :?> int
                SetlQualifier = dict.["setlQualifier"] :?> string
        }), clearingOrg

let readLevel2 (reader:System.Xml.XmlReader) : nodeType * tree list =
    let dict = ["fileFormat","":>obj; "created",0L:>obj; ] |> toDict

    let rec read definitions pointInTime =
        match reader.Name with
        | "fileFormat" as name -> dict.[name] <- readAsString reader ; read definitions pointInTime
        | "created" as name -> dict.[name] <- readAsInt64 reader ; read definitions pointInTime
        | "definitions" as name -> read (Node (readDefinitions reader) :: definitions) pointInTime
        | "pointInTime" as name -> read definitions (Node (readPointInTime reader) :: pointInTime)
        | _ -> definitions, pointInTime
    reader.ReadStartElement()
    let definitions, pointInTime = read [] []
    reader.ReadEndElement()
    SpanXMLLevel2(
        {
                FileFormat = dict.["fileFormat"] :?> string
                Created = dict.["created"] :?> int64
        }), (definitions @ pointInTime)


let readSpanFile (reader:System.Xml.XmlReader) =
    if reader.Name = "spanFile" then  readLevel2 reader else failwith "Unexpected top-level element"

let readXML (reader:System.Xml.XmlReader) =
    Node (SpanXMLTopLevel (new SpanXMLTopLevel()), [Node (readSpanFile reader)])
