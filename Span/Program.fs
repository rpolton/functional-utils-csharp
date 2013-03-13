module Shaftesbury.Span.Program

open Shaftesbury.FSharp.Utils
open Shaftesbury.Span.XMLParser
open Shaftesbury.Span2.XMLParser
open Shaftesbury.Span.TextParser

let XMLfilenames = 
    [
        "ASXCLFEndOfDayRiskParameterFile130306.spn";
        "CFDEndOfDayRiskParameterFile120720.spn";
        "NZFEndOfDayRiskParameterFile130306.spn";
        "SFEEndOfDayRiskParameterFile130128.spn"
    ] |> List.map (fun nm -> @"C:\Users\Bob\development\functional-utils-csharp\Span\"+nm)

[<EntryPoint>]
let main args = 
    if Array.length args <> 0 then
        match args.[0] with
        | "XML" -> 
            let trees = XMLfilenames |> List.map prepareXMLFile |> List.map Shaftesbury.Span.XMLParser.readXML
            0
        | "XML2" ->
            let trees = XMLfilenames |> List.map prepareXMLFile |> List.map Shaftesbury.Span2.XMLParser.readXML
            0
        | _ -> 1
    else
        let filenames = ["";"";""]
        let trees = filenames |> List.map parseFileIntoTree
        0