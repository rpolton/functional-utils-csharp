#load "Utils.fs"
open Shaftesbury.FSharp.Utils
#load "Span2.XMLParserDataTypes.fs"
open Shaftesbury.Span2.XMLParserDataTypes
#load "Span2.XMLParser.fs"
open Shaftesbury.Span2.XMLParser
#load "Span2.QueryTools.fs"
open Shaftesbury.Span2.QueryTools

let XMLfilenames = 
    [
        "ASXCLFEndOfDayRiskParameterFile130306.spn";
        "CFDEndOfDayRiskParameterFile120720.spn";
        "NZFEndOfDayRiskParameterFile130306.spn";
        "SFEEndOfDayRiskParameterFile130128.spn"
    ] |> List.map (fun nm -> @"C:\Users\Bob\development\functional-utils-csharp\Span\"+nm)

let trees = XMLfilenames |> List.map prepareXMLFile |> List.map readXML

let fn (tLeg:SpanXMLTLeg) = tLeg.Cc = "HV"
    
let findTLegs f tree =
    let rec findIt tree acc = 
        match tree with
        | Node (SpanXMLTLeg (record), _) as node when f record -> node :: acc
        | Node (_, trees) -> trees |> List.map (fun node -> findIt node acc) |> List.concat
    findIt tree []

findTLegs fn trees.[0]

let tLegNode f input =
    match input with
    | Node (SpanXMLTLeg (record) as uNode, _) as node when f record -> Some(uNode,node)
    | _ -> None

findNodeWithPath (tLegNode fn) trees.[0]