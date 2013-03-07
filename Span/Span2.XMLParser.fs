module Shaftesbury.Span2.XMLParser

open Shaftesbury.Span2.XMLParserDataTypes

let first (a,b) = a
let second (a,b) = b

let castToFloat (o:obj) = o:?> float
let castToInt (o:obj) = o:?> int
let castToString (o:obj) = o:?> string

let matcher getValue tfm def = match getValue with | true, v -> tfm v | false, _ -> def

let readAsFloat (reader:System.Xml.XmlReader) = reader.ReadElementContentAsDouble()
let readAsInt (reader:System.Xml.XmlReader) = reader.ReadElementContentAsInt()
let readAsString (reader:System.Xml.XmlReader) = reader.ReadElementContentAsString()

let toDictionary keyFn valueFn =
    List.fold (fun (st:System.Collections.Generic.Dictionary<_,_>) elem -> st.Add (keyFn elem, valueFn elem) ; st) (new System.Collections.Generic.Dictionary<_,_>())

// Level 9
let readDiv (reader:System.Xml.XmlReader) =
    let dict = ["val",0.0:>obj;"dtm",0:>obj;"setlDate",0:>obj] |> toDictionary first second
    let rec readDiv' () =
        match reader.Name with
        | "val" as name -> dict.[name] <- readAsFloat reader ; readDiv' ()
        | "dtm" as name -> dict.[name] <- readAsInt reader ; readDiv' ()
        | "setlDate" as name -> dict.[name] <- readAsInt reader ; readDiv' ()
        | _ -> ignore ()
    
    readDiv' ()

    {
        Val = dict.["val"] |> castToFloat;
        Dtm = dict.["dtm"] |> castToInt;
        SetlDate = dict.["setlDate"] |> castToInt;
    }

let readSpanFile (reader:System.Xml.XmlReader) lst : SpanXMLLevel2 list = lst

 // Level 1
let readXML (reader:System.Xml.XmlReader) =
    Node (SpanXMLTopLevel, (if reader.Name = "spanFile" && reader.Read () then readSpanFile reader [] else []))



