module Shaftesbury.Span.XMLParser

 open Shaftesbury.Span.XMLParserDataTypes

 // Level 9
 let rec readDiv (reader:System.Xml.XmlReader) lvl9 =
    match reader.Name with
    | "val" -> SpanXMLDiv.Val (reader.ReadElementContentAsDouble()) :: lvl9 |> readDiv reader
    | "dtm" -> SpanXMLDiv.Dtm (reader.ReadElementContentAsInt()) :: lvl9 |> readDiv reader
    | "setlDate" -> SpanXMLDiv.SetlDate (reader.ReadElementContentAsInt()) :: lvl9 |> readDiv reader
    | _ -> lvl9

 // Level 8
 let rec readRa (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "r" -> SpanXMLRa.R (reader.ReadElementContentAsInt()) :: lvl8 |> readRa reader
   | "a" -> SpanXMLRa.A (reader.ReadElementContentAsDouble()) :: lvl8 |> readRa reader
   | "d" -> SpanXMLRa.D (reader.ReadElementContentAsDouble()) :: lvl8 |> readRa reader
   | _ -> lvl8
 let rec readDivRate (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "val" -> SpanXMLDivRate.Val (reader.ReadElementContentAsDouble()) :: lvl8 |> readDivRate reader
   | "div" -> (SpanXMLDivRate.Div (readDiv (reader.ReadStartElement() ; reader) [] )) :: lvl8 |> readDivRate (reader.ReadEndElement() ; reader)
   | _ -> lvl8
 let rec readUndC (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "exch" -> SpanXMLUndC.Exch (reader.ReadElementContentAsString()) :: lvl8 |> readUndC reader
   | "pfId" -> SpanXMLUndC.PfId (reader.ReadElementContentAsInt()) :: lvl8 |> readUndC reader
   | "cId" -> SpanXMLUndC.CId (reader.ReadElementContentAsInt()) :: lvl8 |> readUndC reader
   | "s" -> SpanXMLUndC.S (reader.ReadElementContentAsString()) :: lvl8 |> readUndC reader
   | "i" -> SpanXMLUndC.I (reader.ReadElementContentAsDouble()) :: lvl8 |> readUndC reader
   | _ -> lvl8
 let rec readIntrRate (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "val" -> SpanXMLIntrRate.Val (reader.ReadElementContentAsDouble()) :: lvl8 |> readIntrRate reader
   | "rl" -> SpanXMLIntrRate.Rl (reader.ReadElementContentAsInt()) :: lvl8 |> readIntrRate reader
   | "cpm" -> SpanXMLIntrRate.Cpm (reader.ReadElementContentAsInt()) :: lvl8 |> readIntrRate reader
   | "exm" -> SpanXMLIntrRate.Exm (reader.ReadElementContentAsInt()) :: lvl8 |> readIntrRate reader
   | _ -> lvl8
 let rec readOpt (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "cId" -> SpanXMLOpt.CId (reader.ReadElementContentAsInt()) :: lvl8 |> readOpt reader
   | "o" -> SpanXMLOpt.O (reader.ReadElementContentAsString()) :: lvl8 |> readOpt reader
   | "k" -> SpanXMLOpt.K (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "p" -> SpanXMLOpt.P (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "pq" -> SpanXMLOpt.Pq (reader.ReadElementContentAsInt()) :: lvl8 |> readOpt reader
   | "d" -> SpanXMLOpt.D (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "cvf" -> SpanXMLOpt.Cvf (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "svf" -> SpanXMLOpt.Svf (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "v" -> SpanXMLOpt.V (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "val" -> SpanXMLOpt.Val (reader.ReadElementContentAsDouble()) :: lvl8 |> readOpt reader
   | "ra" -> (SpanXMLOpt.Ra (readRa (reader.ReadStartElement() ; reader) [] )) :: lvl8 |> readOpt (reader.ReadEndElement() ; reader)
   | _ -> lvl8
 let rec readRate (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "r" -> SpanXMLRate.R (reader.ReadElementContentAsInt()) :: lvl8 |> readRate reader
   | "val" -> SpanXMLRate.Val (reader.ReadElementContentAsDouble()) :: lvl8 |> readRate reader
   | _ -> lvl8
 let rec readScanRate (reader:System.Xml.XmlReader) lvl8 =
   match reader.Name with
   | "r" -> SpanXMLScanRate.R (reader.ReadElementContentAsInt()) :: lvl8 |> readScanRate reader
   | "priceScan" -> SpanXMLScanRate.PriceScan (reader.ReadElementContentAsDouble()) :: lvl8 |> readScanRate reader
   | "priceScanPct" -> SpanXMLScanRate.PriceScanPct (reader.ReadElementContentAsDouble()) :: lvl8 |> readScanRate reader
   | "volScan" -> SpanXMLScanRate.VolScan (reader.ReadElementContentAsDouble()) :: lvl8 |> readScanRate reader
   | "volScanPct" -> SpanXMLScanRate.VolScanPct (reader.ReadElementContentAsDouble()) :: lvl8 |> readScanRate reader
   | _ -> lvl8
 
 // Level 7
 let rec readPriceScanDef (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "mult" -> SpanXMLPriceScanDef.Mult (reader.ReadElementContentAsDouble()) :: lvl7 |> readPriceScanDef reader
   | "numerator" -> SpanXMLPriceScanDef.Numerator (reader.ReadElementContentAsDouble()) :: lvl7 |> readPriceScanDef reader
   | "denominator" -> SpanXMLPriceScanDef.Denominator (reader.ReadElementContentAsDouble()) :: lvl7 |> readPriceScanDef reader
   | _ -> lvl7
 let rec readVolScanDef (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "mult" -> SpanXMLVolScanDef.Mult (reader.ReadElementContentAsDouble()) :: lvl7 |> readVolScanDef reader
   | "numerator" -> SpanXMLVolScanDef.Numerator (reader.ReadElementContentAsDouble()) :: lvl7 |> readVolScanDef reader
   | "denominator" -> SpanXMLVolScanDef.Denominator (reader.ReadElementContentAsDouble()) :: lvl7 |> readVolScanDef reader
   | _ -> lvl7
 let rec readPhy (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "cId" -> SpanXMLPhy.CId (reader.ReadElementContentAsInt()) :: lvl7 |> readPhy reader
   | "pe" -> SpanXMLPhy.Pe (reader.ReadElementContentAsString()) :: lvl7 |> readPhy reader
   | "p" -> SpanXMLPhy.P (reader.ReadElementContentAsDouble()) :: lvl7 |> readPhy reader
   | "d" -> SpanXMLPhy.D (reader.ReadElementContentAsDouble()) :: lvl7 |> readPhy reader
   | "v" -> SpanXMLPhy.V (reader.ReadElementContentAsDouble()) :: lvl7 |> readPhy reader
   | "cvf" -> SpanXMLPhy.Cvf (reader.ReadElementContentAsDouble()) :: lvl7 |> readPhy reader
   | "val" -> SpanXMLPhy.Val (reader.ReadElementContentAsDouble()) :: lvl7 |> readPhy reader
   | "sc" -> SpanXMLPhy.Sc (reader.ReadElementContentAsDouble()) :: lvl7 |> readPhy reader
   | "ra" -> (SpanXMLPhy.Ra (readRa (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readPhy (reader.ReadEndElement() ; reader)
   | _ -> lvl7
 let rec readGroup (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "id" -> SpanXMLGroup.Id (reader.ReadElementContentAsInt()) :: lvl7 |> readGroup reader
   | "aVal" -> SpanXMLGroup.Aval (reader.ReadElementContentAsString()) :: lvl7 |> readGroup reader
   | _ -> lvl7
 let rec readEquity (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "cId" -> SpanXMLEquity.CId (reader.ReadElementContentAsInt()) :: lvl7 |> readEquity reader
   | "isin" -> SpanXMLEquity.Isin (reader.ReadElementContentAsString()) :: lvl7 |> readEquity reader
   | "pe" -> SpanXMLEquity.Pe (reader.ReadElementContentAsString()) :: lvl7 |> readEquity reader
   | "p" -> SpanXMLEquity.P (reader.ReadElementContentAsDouble()) :: lvl7 |> readEquity reader
   | "d" -> SpanXMLEquity.D (reader.ReadElementContentAsDouble()) :: lvl7 |> readEquity reader
   | "v" -> SpanXMLEquity.V (reader.ReadElementContentAsDouble()) :: lvl7 |> readEquity reader
   | "cvf" -> SpanXMLEquity.Cvf (reader.ReadElementContentAsDouble()) :: lvl7 |> readEquity reader
   | "val" -> SpanXMLEquity.Val (reader.ReadElementContentAsDouble()) :: lvl7 |> readEquity reader
   | "sc" -> SpanXMLEquity.Sc (reader.ReadElementContentAsDouble()) :: lvl7 |> readEquity reader
   | "desc" -> SpanXMLEquity.Desc (reader.ReadElementContentAsString()) :: lvl7 |> readEquity reader
   | "type" -> SpanXMLEquity.Type (reader.ReadElementContentAsString()) :: lvl7 |> readEquity reader
   | "subType" -> SpanXMLEquity.SubType (reader.ReadElementContentAsString()) :: lvl7 |> readEquity reader
   | "divRate" -> (SpanXMLEquity.DivRate (readDivRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readEquity (reader.ReadEndElement() ; reader)
   | "ra" -> (SpanXMLEquity.Ra (readRa (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readEquity (reader.ReadEndElement() ; reader)
   | _ -> lvl7
 let rec readUndPf (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "exch" -> SpanXMLUndPf.Exch (reader.ReadElementContentAsString()) :: lvl7 |> readUndPf reader
   | "pfId" -> SpanXMLUndPf.PfId (reader.ReadElementContentAsInt()) :: lvl7 |> readUndPf reader
   | "pfCode" -> SpanXMLUndPf.PfCode (reader.ReadElementContentAsString()) :: lvl7 |> readUndPf reader
   | "pfType" -> SpanXMLUndPf.PfType (reader.ReadElementContentAsString()) :: lvl7 |> readUndPf reader
   | "s" -> SpanXMLUndPf.S (reader.ReadElementContentAsString()) :: lvl7 |> readUndPf reader
   | "i" -> SpanXMLUndPf.I (reader.ReadElementContentAsDouble()) :: lvl7 |> readUndPf reader
   | _ -> lvl7
 let rec readFut (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "cId" -> SpanXMLFut.CId (reader.ReadElementContentAsInt()) :: lvl7 |> readFut reader
   | "pe" -> SpanXMLFut.Pe (reader.ReadElementContentAsInt()) :: lvl7 |> readFut reader
   | "p" -> SpanXMLFut.P (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "d" -> SpanXMLFut.D (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "v" -> SpanXMLFut.V (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "cvf" -> SpanXMLFut.Cvf (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "val" -> SpanXMLFut.Val (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "sc" -> SpanXMLFut.Sc (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "setlDate" -> SpanXMLFut.SetlDate (reader.ReadElementContentAsInt()) :: lvl7 |> readFut reader
   | "t" -> SpanXMLFut.T (reader.ReadElementContentAsDouble()) :: lvl7 |> readFut reader
   | "undC" -> (SpanXMLFut.UndC (readUndC (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readFut (reader.ReadEndElement() ; reader)
   | "ra" -> (SpanXMLFut.Ra (readRa (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readFut (reader.ReadEndElement() ; reader)
   | "scanRate" -> (SpanXMLFut.ScanRate (readScanRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readFut (reader.ReadEndElement() ; reader)
   | _ -> lvl7
 let rec readSeries (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "pe" -> SpanXMLSeries.Pe (reader.ReadElementContentAsInt()) :: lvl7 |> readSeries reader
   | "v" -> SpanXMLSeries.V (reader.ReadElementContentAsDouble()) :: lvl7 |> readSeries reader
   | "volSrc" -> SpanXMLSeries.VolSrc (reader.ReadElementContentAsString()) :: lvl7 |> readSeries reader
   | "setlDate" -> SpanXMLSeries.SetlDate (reader.ReadElementContentAsInt()) :: lvl7 |> readSeries reader
   | "t" -> SpanXMLSeries.T (reader.ReadElementContentAsDouble()) :: lvl7 |> readSeries reader
   | "cvf" -> SpanXMLSeries.Cvf (reader.ReadElementContentAsDouble()) :: lvl7 |> readSeries reader
   | "svf" -> SpanXMLSeries.Svf (reader.ReadElementContentAsDouble()) :: lvl7 |> readSeries reader
   | "sc" -> SpanXMLSeries.Sc (reader.ReadElementContentAsDouble()) :: lvl7 |> readSeries reader
   | "undC" -> (SpanXMLSeries.UndC(readUndC (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readSeries (reader.ReadEndElement() ; reader)
   | "divRate" -> (SpanXMLSeries.DivRate (readDivRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readSeries (reader.ReadEndElement() ; reader)
   | "intrRate" -> (SpanXMLSeries.IntrRate (readIntrRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readSeries (reader.ReadEndElement() ; reader)
   | "scanRate" -> (SpanXMLSeries.ScanRate (readScanRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readSeries (reader.ReadEndElement() ; reader)
   | "opt" -> (SpanXMLSeries.Opt (readOpt (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readSeries (reader.ReadEndElement() ; reader)
   | _ -> lvl7
 let rec readTier (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "tn" -> SpanXMLTier.Tn (reader.ReadElementContentAsInt()) :: lvl7 |> readTier reader
   | "ePe" -> SpanXMLTier.EPe (reader.ReadElementContentAsInt()) :: lvl7 |> readTier reader
   | "sPe" -> SpanXMLTier.SPe (reader.ReadElementContentAsInt()) :: lvl7 |> readTier reader
   | _ -> lvl7
 let rec readTierWithRate (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "tn" -> SpanXMLTierWithRate.Tn (reader.ReadElementContentAsInt()) :: lvl7 |> readTierWithRate reader
   | "rate" -> (SpanXMLTierWithRate.Rate (readRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readTierWithRate (reader.ReadEndElement() ; reader)
   | _ -> lvl7
 let rec readTierWithScanRate (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "tn" -> SpanXMLTierWithScanRate.Tn (reader.ReadElementContentAsInt()) :: lvl7 |> readTierWithScanRate reader
   | "sPe" -> SpanXMLTierWithScanRate.SPe (reader.ReadElementContentAsInt()) :: lvl7 |> readTierWithScanRate reader
   | "ePe" -> SpanXMLTierWithScanRate.EPe (reader.ReadElementContentAsInt()) :: lvl7 |> readTierWithScanRate reader
   | "scanRate" -> (SpanXMLTierWithScanRate.ScanRate (readScanRate (reader.ReadStartElement() ; reader) [] )) :: lvl7 |> readTierWithScanRate (reader.ReadEndElement() ; reader)
   | _ -> lvl7
 let rec readTLeg (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "cc" -> SpanXMLTLeg.Cc (reader.ReadElementContentAsString()) :: lvl7 |> readTLeg reader
   | "tn" -> SpanXMLTLeg.Tn (reader.ReadElementContentAsInt()) :: lvl7 |> readTLeg reader
   | "rs" -> SpanXMLTLeg.Rs (reader.ReadElementContentAsString()) :: lvl7 |> readTLeg reader
   | "i" -> SpanXMLTLeg.I (reader.ReadElementContentAsDouble()) :: lvl7 |> readTLeg reader
   | _ -> lvl7
 let rec readPLeg (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "cc" -> SpanXMLPLeg.Cc (reader.ReadElementContentAsString()) :: lvl7 |> readPLeg reader
   | "pe" -> SpanXMLPLeg.Pe (reader.ReadElementContentAsInt()) :: lvl7 |> readPLeg reader
   | "rs" -> SpanXMLPLeg.Rs (reader.ReadElementContentAsString()) :: lvl7 |> readPLeg reader
   | "i" -> SpanXMLPLeg.I (reader.ReadElementContentAsDouble()) :: lvl7 |> readPLeg reader
   | _ -> lvl7
 let rec readSLeg (reader:System.Xml.XmlReader) lvl7 =
   match reader.Name with
   | "cc" -> SpanXMLSLeg.Cc (reader.ReadElementContentAsString()) :: lvl7 |> readSLeg reader
   | "isTarget" -> SpanXMLSLeg.IsTarget (reader.ReadElementContentAsInt()) :: lvl7 |> readSLeg reader
   | "isRequired" -> SpanXMLSLeg.IsRequired (reader.ReadElementContentAsInt()) :: lvl7 |> readSLeg reader
   | _ -> lvl7
 
 // Level 6
 let rec readScanPointDef (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "point" -> SpanXMLScanPointDef.Point (reader.ReadElementContentAsInt()) :: lvl6 |> readScanPointDef reader
   | "priceScanDef" -> (SpanXMLScanPointDef.PriceScanDef (readPriceScanDef (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readScanPointDef (reader.ReadEndElement() ; reader)
   | "volScanDef" -> (SpanXMLScanPointDef.VolScanDef (readVolScanDef (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readScanPointDef (reader.ReadEndElement() ; reader)
   | "weight" -> SpanXMLScanPointDef.Weight (reader.ReadElementContentAsDouble()) :: lvl6 |> readScanPointDef reader
   | "pairedPoint" -> SpanXMLScanPointDef.PairedPoint (reader.ReadElementContentAsInt()) :: lvl6 |> readScanPointDef reader
   | _ -> lvl6
 let rec readDeltaPointDef (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "point" -> SpanXMLDeltaPointDef.Point (reader.ReadElementContentAsInt()) :: lvl6 |> readDeltaPointDef reader
   | "priceScanDef" -> (SpanXMLDeltaPointDef.PriceScanDef (readPriceScanDef (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readDeltaPointDef (reader.ReadEndElement() ; reader)
   | "volScanDef" -> (SpanXMLDeltaPointDef.VolScanDef (readVolScanDef (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readDeltaPointDef (reader.ReadEndElement() ; reader)
   | "weight" -> SpanXMLDeltaPointDef.Weight (reader.ReadElementContentAsDouble()) :: lvl6 |> readDeltaPointDef reader
   | _ -> lvl6
 let rec readPhyPf (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "pfId" -> SpanXMLPhyPf.PfId (reader.ReadElementContentAsInt()) :: lvl6 |> readPhyPf reader
   | "pfCode" -> SpanXMLPhyPf.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "name" -> SpanXMLPhyPf.Name (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "currency" -> SpanXMLPhyPf.Currency (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "cvf" -> SpanXMLPhyPf.Cvf (reader.ReadElementContentAsDouble()) :: lvl6 |> readPhyPf reader
   | "priceDl" -> SpanXMLPhyPf.PriceDl (reader.ReadElementContentAsInt()) :: lvl6 |> readPhyPf reader
   | "priceFmt" -> SpanXMLPhyPf.PriceFmt (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "valueMeth" -> SpanXMLPhyPf.ValueMeth (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "priceMeth" -> SpanXMLPhyPf.PriceMeth (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "setlMeth" -> SpanXMLPhyPf.SetlMeth (reader.ReadElementContentAsString()) :: lvl6 |> readPhyPf reader
   | "positionsAllowed" -> SpanXMLPhyPf.PositionsAllowed (reader.ReadElementContentAsInt()) :: lvl6 |> readPhyPf reader
   | "phy" -> (SpanXMLPhyPf.Phy (readPhy (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readPhyPf (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readEquityPf (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "pfId" -> SpanXMLEquityPf.PfId (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "pfCode" -> SpanXMLEquityPf.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "group" -> (SpanXMLEquityPf.Group (readGroup (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readEquityPf (reader.ReadEndElement() ; reader)
   | "name" -> SpanXMLEquityPf.Name (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "currency" -> SpanXMLEquityPf.Currency (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "cvf" -> SpanXMLEquityPf.Cvf (reader.ReadElementContentAsDouble()) :: lvl6 |> readEquityPf reader
   | "priceDl" -> SpanXMLEquityPf.PriceDl (reader.ReadElementContentAsInt()) :: lvl6 |> readEquityPf reader
   | "priceFmt" -> SpanXMLEquityPf.PriceFmt (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "valueMeth" -> SpanXMLEquityPf.ValueMeth (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "priceMeth" -> SpanXMLEquityPf.PriceMeth (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "setlMeth" -> SpanXMLEquityPf.SetlMeth (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "country" -> SpanXMLEquityPf.Country (reader.ReadElementContentAsString()) :: lvl6 |> readEquityPf reader
   | "equity" -> (SpanXMLEquityPf.Equity (readEquity (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readEquityPf (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readFutPf (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "pfId" -> SpanXMLFutPf.PfId (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "pfCode" -> SpanXMLFutPf.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "group" -> (SpanXMLFutPf.Group (readGroup (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readFutPf (reader.ReadEndElement() ; reader)
   | "name" -> SpanXMLFutPf.Name (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "currency" -> SpanXMLFutPf.Currency (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "cvf" -> SpanXMLFutPf.Cvf (reader.ReadElementContentAsDouble()) :: lvl6 |> readFutPf reader
   | "priceDl" -> SpanXMLFutPf.PriceDl (reader.ReadElementContentAsInt()) :: lvl6 |> readFutPf reader
   | "priceFmt" -> SpanXMLFutPf.PriceFmt (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "valueMeth" -> SpanXMLFutPf.ValueMeth (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "priceMeth" -> SpanXMLFutPf.PriceMeth (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "setlMeth" -> SpanXMLFutPf.SetlMeth (reader.ReadElementContentAsString()) :: lvl6 |> readFutPf reader
   | "positionsAllowed" -> SpanXMLFutPf.PositionsAllowed (reader.ReadElementContentAsInt()) :: lvl6 |> readFutPf reader
   | "undPf" -> (SpanXMLFutPf.UndPf (readUndPf (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readFutPf (reader.ReadEndElement() ; reader)
   | "fut" -> (SpanXMLFutPf.Fut (readFut (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readFutPf (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readOopPf (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "pfId" -> SpanXMLOopPf.PfId (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "pfCode" -> SpanXMLOopPf.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "group" -> (SpanXMLOopPf.Group (readGroup (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOopPf (reader.ReadEndElement() ; reader)
   | "name" -> SpanXMLOopPf.Name (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "exercise" -> SpanXMLOopPf.Exercise (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "currency" -> SpanXMLOopPf.Currency (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "cvf" -> SpanXMLOopPf.Cvf (reader.ReadElementContentAsDouble()) :: lvl6 |> readOopPf reader
   | "priceDl" -> SpanXMLOopPf.PriceDl (reader.ReadElementContentAsInt()) :: lvl6 |> readOopPf reader
   | "priceFmt" -> SpanXMLOopPf.PriceFmt (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "strikeDl" -> SpanXMLOopPf.StrikeDl (reader.ReadElementContentAsInt()) :: lvl6 |> readOopPf reader
   | "strikeFmt" -> SpanXMLOopPf.StrikeFmt (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "cab" -> SpanXMLOopPf.Cab (reader.ReadElementContentAsDouble()) :: lvl6 |> readOopPf reader
   | "valueMeth" -> SpanXMLOopPf.ValueMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "priceMeth" -> SpanXMLOopPf.PriceMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "setlMeth" -> SpanXMLOopPf.SetlMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "priceModel" -> SpanXMLOopPf.PriceModel (reader.ReadElementContentAsString()) :: lvl6 |> readOopPf reader
   | "undPf" -> (SpanXMLOopPf.UndPf (readUndPf (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOopPf (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readOofPf (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "pfId" -> SpanXMLOofPf.PfId (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "pfCode" -> SpanXMLOofPf.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "group" -> (SpanXMLOofPf.Group (readGroup (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOofPf (reader.ReadEndElement() ; reader)
   | "name" -> SpanXMLOofPf.Name (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "exercise" -> SpanXMLOofPf.Exercise (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "currency" -> SpanXMLOofPf.Currency (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "cvf" -> SpanXMLOofPf.Cvf (reader.ReadElementContentAsDouble()) :: lvl6 |> readOofPf reader
   | "priceDl" -> SpanXMLOofPf.PriceDl (reader.ReadElementContentAsInt()) :: lvl6 |> readOofPf reader
   | "priceFmt" -> SpanXMLOofPf.PriceFmt (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "strikeDl" -> SpanXMLOofPf.StrikeDl (reader.ReadElementContentAsInt()) :: lvl6 |> readOofPf reader
   | "strikeFmt" -> SpanXMLOofPf.StrikeFmt (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "isVariableTick" -> SpanXMLOofPf.IsVariableTick (reader.ReadElementContentAsInt()) :: lvl6 |> readOofPf reader
   | "cab" -> SpanXMLOofPf.Cab (reader.ReadElementContentAsDouble()) :: lvl6 |> readOofPf reader
   | "valueMeth" -> SpanXMLOofPf.ValueMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "priceMeth" -> SpanXMLOofPf.PriceMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "setlMeth" -> SpanXMLOofPf.SetlMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "priceModel" -> SpanXMLOofPf.PriceModel (reader.ReadElementContentAsString()) :: lvl6 |> readOofPf reader
   | "undPf" -> (SpanXMLOofPf.UndPf (readUndPf (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOofPf (reader.ReadEndElement() ; reader)
   | "series" -> (SpanXMLOofPf.Series (readSeries (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOofPf (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readOoePf (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "pfId" -> SpanXMLOoePf.PfId (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "pfCode" -> SpanXMLOoePf.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "group" -> (SpanXMLOoePf.Group (readGroup (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOoePf (reader.ReadEndElement() ; reader)
   | "name" -> SpanXMLOoePf.Name (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "exercise" -> SpanXMLOoePf.Exercise (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "currency" -> SpanXMLOoePf.Currency (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "cvf" -> SpanXMLOoePf.Cvf (reader.ReadElementContentAsDouble()) :: lvl6 |> readOoePf reader
   | "priceDl" -> SpanXMLOoePf.PriceDl (reader.ReadElementContentAsInt()) :: lvl6 |> readOoePf reader
   | "priceFmt" -> SpanXMLOoePf.PriceFmt (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "strikeDl" -> SpanXMLOoePf.StrikeDl (reader.ReadElementContentAsInt()) :: lvl6 |> readOoePf reader
   | "strikeFmt" -> SpanXMLOoePf.StrikeFmt (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "cab" -> SpanXMLOoePf.Cab (reader.ReadElementContentAsDouble()) :: lvl6 |> readOoePf reader
   | "valueMeth" -> SpanXMLOoePf.ValueMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "priceMeth" -> SpanXMLOoePf.PriceMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "setlMeth" -> SpanXMLOoePf.SetlMeth (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "priceModel" -> SpanXMLOoePf.PriceModel (reader.ReadElementContentAsString()) :: lvl6 |> readOoePf reader
   | "undPf" -> (SpanXMLOoePf.UndPf (readUndPf (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOoePf (reader.ReadEndElement() ; reader)
   | "series" -> (SpanXMLOoePf.Series (readSeries (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readOoePf (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readPfLink (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "exch" -> SpanXMLPfLink.Exch (reader.ReadElementContentAsString()) :: lvl6 |> readPfLink reader
   | "pfId" -> SpanXMLPfLink.PfId (reader.ReadElementContentAsInt()) :: lvl6 |> readPfLink reader
   | "pfCode" -> SpanXMLPfLink.PfCode (reader.ReadElementContentAsString()) :: lvl6 |> readPfLink reader
   | "pfType" -> SpanXMLPfLink.PfType (reader.ReadElementContentAsString()) :: lvl6 |> readPfLink reader
   | "sc" -> SpanXMLPfLink.Sc (reader.ReadElementContentAsDouble()) :: lvl6 |> readPfLink reader
   | "cmbMeth" -> SpanXMLPfLink.CmbMeth (reader.ReadElementContentAsString()) :: lvl6 |> readPfLink reader
   | "applyBasisRisk" -> SpanXMLPfLink.ApplyBasisRisk (reader.ReadElementContentAsInt()) :: lvl6 |> readPfLink reader
   | "oopDeltaMeth" -> SpanXMLPfLink.OopDeltaMeth (reader.ReadElementContentAsString()) :: lvl6 |> readPfLink reader
   | _ -> lvl6
 let rec readIntraTiers (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "tier" -> (readTier (reader.ReadStartElement() ; reader) [] |> List.map (fun pit -> SpanXMLIntraTiers.Tier (pit))) @ lvl6 |> readIntraTiers (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readInterTiers (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "tier" -> (readTier (reader.ReadStartElement() ; reader) [] |> List.map (fun pit -> SpanXMLInterTiers.Tier (pit))) @ lvl6 |> readInterTiers (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readSomTiers (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "tier" -> (readTierWithRate (reader.ReadStartElement() ; reader) [] |> List.map (fun pit -> SpanXMLSomTiers.Tier (pit))) @ lvl6 |> readSomTiers (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readRateTiers (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "tier" -> (readTierWithScanRate (reader.ReadStartElement() ; reader) [] |> List.map (fun pit -> SpanXMLRateTiers.Tier (pit))) @ lvl6 |> readRateTiers (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readDSpread (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "spread" -> SpanXMLDSpread.Spread (reader.ReadElementContentAsInt()) :: lvl6 |> readDSpread reader
   | "chargeMeth" -> SpanXMLDSpread.ChargeMeth (reader.ReadElementContentAsString()) :: lvl6 |> readDSpread reader
   | "rate" -> (SpanXMLDSpread.Rate (readRate (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readDSpread (reader.ReadEndElement() ; reader)
   | "tLeg" -> (SpanXMLDSpread.TLeg (readTLeg (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readDSpread (reader.ReadEndElement() ; reader)
   | "pLeg" -> (SpanXMLDSpread.PLeg (readPLeg (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readDSpread (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readHSpread (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "spread" -> SpanXMLHSpread.Spread (reader.ReadElementContentAsInt()) :: lvl6 |> readHSpread reader
   | "rate" -> (SpanXMLHSpread.Rate (readRate (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readHSpread (reader.ReadEndElement() ; reader)
   | "tLeg" -> (SpanXMLHSpread.TLeg (readTLeg (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readHSpread (reader.ReadEndElement() ; reader)
   | "sLeg" -> (SpanXMLHSpread.SLeg (readSLeg (reader.ReadStartElement() ; reader) [] )) :: lvl6 |> readHSpread (reader.ReadEndElement() ; reader)
   | _ -> lvl6
 let rec readSpotRate (reader:System.Xml.XmlReader) lvl6 =
   match reader.Name with
   | "r" -> SpanXMLSpotRate.R (reader.ReadElementContentAsInt()) :: lvl6 |> readSpotRate reader
   | "pe" -> SpanXMLSpotRate.Pe (reader.ReadElementContentAsInt()) :: lvl6 |> readSpotRate reader
   | "sprd" -> SpanXMLSpotRate.Sprd (reader.ReadElementContentAsDouble()) :: lvl6 |> readSpotRate reader
   | "outr" -> SpanXMLSpotRate.Outr (reader.ReadElementContentAsDouble()) :: lvl6 |> readSpotRate reader
   | _ -> lvl6

 // Level 5
 let rec readCurConv (reader:System.Xml.XmlReader) lvl5 =
   match reader.Name with
   | "fromCur" -> SpanXMLCurConv.FromCur (reader.ReadElementContentAsString()) :: lvl5 |> readCurConv reader
   | "toCur" -> SpanXMLCurConv.ToCur (reader.ReadElementContentAsString()) :: lvl5 |> readCurConv reader
   | "factor" -> SpanXMLCurConv.Factor (reader.ReadElementContentAsDouble()) :: lvl5 |> readCurConv reader
   | _ -> lvl5
 let rec readPbRateDef (reader:System.Xml.XmlReader) lvl5 =
   match reader.Name with
   | "r" -> SpanXMLPbRateDef.R (reader.ReadElementContentAsInt()) :: lvl5 |> readPbRateDef reader
   | "isCust" -> SpanXMLPbRateDef.IsCust (reader.ReadElementContentAsInt()) :: lvl5 |> readPbRateDef reader
   | "acctType" -> SpanXMLPbRateDef.AcctType (reader.ReadElementContentAsString()) :: lvl5 |> readPbRateDef reader
   | "isM" -> SpanXMLPbRateDef.IsM (reader.ReadElementContentAsInt()) :: lvl5 |> readPbRateDef reader
   | "pbc" -> SpanXMLPbRateDef.Pbc (reader.ReadElementContentAsString()) :: lvl5 |> readPbRateDef reader
   | _ -> lvl5
 let rec readPointDef (reader:System.Xml.XmlReader) lvl5 =
   match reader.Name with
   | "r" -> SpanXMLPointDef.R (reader.ReadElementContentAsInt()) :: lvl5 |> readPointDef reader
   | "scanPointDef" -> (SpanXMLPointDef.ScanPointDef (readScanPointDef (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readPointDef (reader.ReadEndElement() ; reader)
   | "deltaPointDef" -> (SpanXMLPointDef.DeltaPointDef (readDeltaPointDef (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readPointDef (reader.ReadEndElement() ; reader)
   | _ -> lvl5
 let rec readExchange (reader:System.Xml.XmlReader) lvl5 =
   match reader.Name with
   | "exch" -> SpanXMLExchange.Exch (reader.ReadElementContentAsString()) :: lvl5 |> readExchange reader
   | "name" -> SpanXMLExchange.Name (reader.ReadElementContentAsString()) :: lvl5 |> readExchange reader
   | "phyPf" -> (SpanXMLExchange.PhyPf (readPhyPf (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readExchange (reader.ReadEndElement() ; reader)
   | "equityPf" -> (SpanXMLExchange.EquityPf (readEquityPf (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readExchange (reader.ReadEndElement() ; reader)
   | "futPf" -> (SpanXMLExchange.FutPf (readFutPf (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readExchange (reader.ReadEndElement() ; reader)
   | "oofPf" -> (SpanXMLExchange.OofPf (readOofPf (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readExchange (reader.ReadEndElement() ; reader)
   | "oopPf" -> (SpanXMLExchange.OopPf (readOopPf (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readExchange (reader.ReadEndElement() ; reader)
   | "ooePf" -> (SpanXMLExchange.OoePf (readOoePf (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readExchange (reader.ReadEndElement() ; reader)
   | _ -> lvl5
 let rec readCcDef (reader:System.Xml.XmlReader) lvl5 =
   match reader.Name with
   | "cc" -> SpanXMLCcDef.Cc (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "name" -> SpanXMLCcDef.Name (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "currency" -> SpanXMLCcDef.Currency (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "riskExponent" -> SpanXMLCcDef.RiskExponent (reader.ReadElementContentAsInt()) :: lvl5 |> readCcDef reader
   | "capAnov" -> SpanXMLCcDef.CapAnov (reader.ReadElementContentAsInt()) :: lvl5 |> readCcDef reader
   | "procMeth" -> SpanXMLCcDef.ProcMeth (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "wfprMeth" -> SpanXMLCcDef.WfprMeth (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "spotMeth" -> SpanXMLCcDef.SpotMeth (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "somMeth" -> SpanXMLCcDef.SomMeth (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "cmbMeth" -> SpanXMLCcDef.CmbMeth (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "marginMeth" -> SpanXMLCcDef.MarginMeth (reader.ReadElementContentAsString()) :: lvl5 |> readCcDef reader
   | "factorCurveSetId" -> SpanXMLCcDef.FactorCurveSetId (reader.ReadElementContentAsInt()) :: lvl5 |> readCcDef reader
   | "factorScenarioSetId" -> SpanXMLCcDef.FactorScenarioSetId (reader.ReadElementContentAsInt()) :: lvl5 |> readCcDef reader
   | "interCurScan" -> SpanXMLCcDef.InterCurScan (reader.ReadElementContentAsInt()) :: lvl5 |> readCcDef reader
   | "spotRate" -> (SpanXMLCcDef.SpotRate (readSpotRate (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "limitArraysTo16Points" -> SpanXMLCcDef.LimitArraysTo16Points (reader.ReadElementContentAsInt()) :: lvl5 |> readCcDef reader
   | "pfLink" -> (SpanXMLCcDef.PfLink (readPfLink (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "intraTiers" -> (SpanXMLCcDef.IntraTiers (readIntraTiers (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "interTiers" -> (SpanXMLCcDef.InterTiers (readInterTiers (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "somTiers" -> (SpanXMLCcDef.SomTiers (readSomTiers (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "rateTiers" -> (SpanXMLCcDef.RateTiers (readRateTiers (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "intrRate" -> (SpanXMLCcDef.IntrRate (readIntrRate (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "dSpread" -> (SpanXMLCcDef.DSpread (readDSpread (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | "group" -> (SpanXMLCcDef.Group (readGroup (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readCcDef (reader.ReadEndElement() ; reader)
   | _ -> lvl5
 let rec readInterSpreads (reader:System.Xml.XmlReader) lvl5 =
   match reader.Name with
   | "dSpread" -> (SpanXMLInterSpreads.DSpread (readDSpread (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readInterSpreads (reader.ReadEndElement() ; reader)
   | "hSpread" -> (SpanXMLInterSpreads.HSpread (readHSpread (reader.ReadStartElement() ; reader) [] )) :: lvl5 |> readInterSpreads (reader.ReadEndElement() ; reader)
   | _ -> lvl5
 
 // Level 4
 let rec readCurrencyDef (reader:System.Xml.XmlReader) lvl4 =
   match reader.Name with
   | "currency" -> Currency (reader.ReadElementContentAsString()) :: lvl4 |> readCurrencyDef reader
   | "symbol" -> Symbol (reader.ReadElementContentAsString()) :: lvl4 |> readCurrencyDef reader
   | "name" -> SpanXMLCurrencyDef.Name (reader.ReadElementContentAsString()) :: lvl4 |> readCurrencyDef reader
   | "decimalPos" -> DecimalPos (reader.ReadElementContentAsInt()) :: lvl4 |> readCurrencyDef reader
   | _ -> lvl4
 let rec readAcctTypeDef (reader:System.Xml.XmlReader) lvl4 =
   match reader.Name with
   | "isCust" -> SpanXMLAcctTypeDef.IsCust (reader.ReadElementContentAsInt()) :: lvl4 |> readAcctTypeDef reader
   | "acctType" -> SpanXMLAcctTypeDef.AcctType (reader.ReadElementContentAsString()) :: lvl4 |> readAcctTypeDef reader
   | "name" -> SpanXMLAcctTypeDef.Name (reader.ReadElementContentAsString()) :: lvl4 |> readAcctTypeDef reader
   | "isNetMargin" -> SpanXMLAcctTypeDef.IsNetMargin (reader.ReadElementContentAsInt()) :: lvl4 |> readAcctTypeDef reader
   | "priority" -> SpanXMLAcctTypeDef.Priority (reader.ReadElementContentAsInt()) :: lvl4 |> readAcctTypeDef reader
   | _ -> lvl4
 let rec readAcctSubTypeDef (reader:System.Xml.XmlReader) lvl4 =
   match reader.Name with
   | "acctSubTypeCode" -> SpanXMLAcctSubTypeDef.AcctSubTypeCode (reader.ReadElementContentAsString()) :: lvl4 |> readAcctSubTypeDef reader
   | "dataType" -> SpanXMLAcctSubTypeDef.DataType (reader.ReadElementContentAsString()) :: lvl4 |> readAcctSubTypeDef reader
   | "description" -> SpanXMLAcctSubTypeDef.Description (reader.ReadElementContentAsString()) :: lvl4 |> readAcctSubTypeDef reader
   | _ -> lvl4
 let rec readGroupTypeDef (reader:System.Xml.XmlReader) lvl4 =
   match reader.Name with
   | "id" -> SpanXMLGroupTypeDef.Id (reader.ReadElementContentAsInt()) :: lvl4 |> readGroupTypeDef reader
   | "name" -> SpanXMLGroupTypeDef.Name (reader.ReadElementContentAsString()) :: lvl4 |> readGroupTypeDef reader
   | _ -> lvl4
 let rec readGroupDef (reader:System.Xml.XmlReader) lvl4 =
   match reader.Name with
   | "id" -> SpanXMLGroupDef.Id (reader.ReadElementContentAsInt()) :: lvl4 |> readGroupDef reader
   | "aVal" -> SpanXMLGroupDef.Aval (reader.ReadElementContentAsString()) :: lvl4 |> readGroupDef reader
   | "description" -> SpanXMLGroupDef.Description (reader.ReadElementContentAsString()) :: lvl4 |> readGroupDef reader
   | _ -> lvl4
 let rec readClearingOrg (reader:System.Xml.XmlReader) lvl4 =
   match reader.Name with
   | "ec" -> SpanXMLClearingOrg.Ec (reader.ReadElementContentAsString()) :: lvl4 |> readClearingOrg reader
   | "name" -> SpanXMLClearingOrg.Name (reader.ReadElementContentAsString()) :: lvl4 |> readClearingOrg reader
   | "isContractScale" -> SpanXMLClearingOrg.IsContractScale (reader.ReadElementContentAsInt()) :: lvl4 |> readClearingOrg reader
   | "isNetMargin" -> SpanXMLClearingOrg.IsNetMargin (reader.ReadElementContentAsInt()) :: lvl4 |> readClearingOrg reader
   | "finalizeMeth" -> SpanXMLClearingOrg.FinalizeMeth (reader.ReadElementContentAsString()) :: lvl4 |> readClearingOrg reader
   | "oopDeltaMeth" -> SpanXMLClearingOrg.OopDeltaMeth (reader.ReadElementContentAsString()) :: lvl4 |> readClearingOrg reader
   | "capAnov" -> SpanXMLClearingOrg.CapAnov (reader.ReadElementContentAsInt()) :: lvl4 |> readClearingOrg reader
   | "lookAheadYears" -> SpanXMLClearingOrg.LookAheadYears (reader.ReadElementContentAsDouble()) :: lvl4 |> readClearingOrg reader
   | "lookAheadDays" -> SpanXMLClearingOrg.LookAheadDays (reader.ReadElementContentAsInt()) :: lvl4 |> readClearingOrg reader
   | "daysPerYear" -> SpanXMLClearingOrg.DaysPerYear (reader.ReadElementContentAsInt()) :: lvl4 |> readClearingOrg reader
   | "limitSubAccountOffset" -> SpanXMLClearingOrg.LimitSubAccountOffset (reader.ReadElementContentAsInt()) :: lvl4 |> readClearingOrg reader
   | "curConv" -> (SpanXMLClearingOrg.CurConv (readCurConv (reader.ReadStartElement() ; reader) [] )) :: lvl4 |> readClearingOrg (reader.ReadEndElement() ; reader)
   | "pbRateDef" -> (SpanXMLClearingOrg.PbRateDef (readPbRateDef (reader.ReadStartElement() ; reader) [] )) :: lvl4 |> readClearingOrg (reader.ReadEndElement() ; reader)
   | "pointDef" -> (SpanXMLClearingOrg.PointDef (readPointDef (reader.ReadStartElement() ; reader) [] )) :: lvl4 |> readClearingOrg (reader.ReadEndElement() ; reader)
   | "exchange" -> (SpanXMLClearingOrg.Exchange (readExchange (reader.ReadStartElement() ; reader) [] )) :: lvl4 |> readClearingOrg (reader.ReadEndElement() ; reader)
   | "ccDef" -> (SpanXMLClearingOrg.CcDef (readCcDef (reader.ReadStartElement() ; reader) [] )) :: lvl4 |> readClearingOrg (reader.ReadEndElement() ; reader)
   | "interSpreads" -> (SpanXMLClearingOrg.InterSpreads (readInterSpreads (reader.ReadStartElement() ; reader) [] )) :: lvl4 |> readClearingOrg (reader.ReadEndElement() ; reader)
   | _ -> lvl4
 
 // Level 3
 let rec readDefinition (reader:System.Xml.XmlReader) (lvl3:SpanXMLDefinition list) : SpanXMLDefinition list =
   match reader.Name with
   | "currencyDef" -> (CurrencyDef (readCurrencyDef (reader.ReadStartElement() ; reader) [] )) :: lvl3 |> readDefinition (reader.ReadEndElement() ; reader)
   | "acctTypeDef" -> (AcctTypeDef (readAcctTypeDef (reader.ReadStartElement() ; reader) [] )) :: lvl3 |> readDefinition (reader.ReadEndElement() ; reader)
   | "acctSubTypeDef" -> (AcctSubTypeDef (readAcctSubTypeDef (reader.ReadStartElement() ; reader) [] )) :: lvl3 |> readDefinition (reader.ReadEndElement() ; reader)
   | "groupTypeDef" -> (GroupTypeDef (readGroupTypeDef (reader.ReadStartElement() ; reader) [] )) :: lvl3 |> readDefinition (reader.ReadEndElement() ; reader)
   | "groupDef" -> (GroupDef (readGroupDef (reader.ReadStartElement() ; reader) [] )) :: lvl3 |> readDefinition (reader.ReadEndElement() ; reader)
   | _ -> lvl3
 let rec readPointInTime (reader:System.Xml.XmlReader) lvl3 =
   match reader.Name with
   | "date" -> (Date (reader.ReadElementContentAsInt()) :: lvl3) |> readPointInTime reader
   | "isSetl" -> (IsSetl (reader.ReadElementContentAsInt()) :: lvl3) |> readPointInTime reader
   | "setlQualifier" -> (SetlQualifier (reader.ReadElementContentAsString()) :: lvl3) |> readPointInTime reader
   | "clearingOrg" -> (ClearingOrg (readClearingOrg (reader.ReadStartElement() ; reader) [] )) :: lvl3 |> readPointInTime (reader.ReadEndElement() ; reader)
   | _ -> lvl3

 // Level 2
 let rec readSpanFile (reader:System.Xml.XmlReader) lvl2 =
   match reader.Name with
   | "fileFormat" -> (FileFormat (reader.ReadElementContentAsString()) :: lvl2) |> readSpanFile reader
   | "created" -> (Created (reader.ReadElementContentAsLong()) :: lvl2) |> readSpanFile reader
   | "definitions" -> (Definitions (readDefinition (reader.ReadStartElement() ; reader) [] )) :: lvl2 |> readSpanFile (reader.ReadEndElement() ; reader)
   | "pointInTime" -> (PointInTime (readPointInTime (reader.ReadStartElement() ; reader) [] )) :: lvl2 |> readSpanFile (reader.ReadEndElement() ; reader)
   | _ -> lvl2
 
 // Level 1
 let readXML (reader:System.Xml.XmlReader) =
   SpanFile (if reader.Name = "spanFile" && reader.Read () then readSpanFile reader [] else [])
