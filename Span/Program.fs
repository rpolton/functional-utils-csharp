module Shaftesbury.Span.Program

open Shaftesbury.FSharp.Utils
open Shaftesbury.Span.XMLParser
open Shaftesbury.Span2.XMLParser
open Shaftesbury.Span.LCH.TextParser
open Shaftesbury.Span.CME.ExpandedFormat

let XMLfilenames = 
    [
        "ASXCLFEndOfDayRiskParameterFile130306.spn";
        "CFDEndOfDayRiskParameterFile120720.spn";
        "NZFEndOfDayRiskParameterFile130306.spn";
        "SFEEndOfDayRiskParameterFile130128.spn"
    ] |> List.map (fun nm -> @"C:\Users\Bob\development\data\Span\"+nm)

let HKfilenames = 
    [
        "rci______-_____-__-_____-130314-0844.lis";
        "rci______-_____-__-_____-130314-0948.lis";
        "rci______-_____-__-_____-130314-1037.lis";
        "rci______-_____-__-_____-130314-1242.lis";
        "rcp______-_____-__-_____-130314.lis";
        "rpi______-_____-__-_____-130314-0844.lis";
        "rpi______-_____-__-_____-130314-0948.lis";
        "rpi______-_____-__-_____-130314-1037.lis";
        "rpi______-_____-__-_____-130314-1242.lis";
        "rpp______-_____-__-_____-130314.lis";
    ]
    |> List.map (fun nm -> @"C:\Users\Bob\development\data\Span\RPF_130314\"+nm)


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
        | "HK" ->
            let splitRows =
                HKfilenames |> 
                List.map (fun filename ->
                            let filename = HKfilenames.[0]
                            use fs = new System.IO.StreamReader(filename)
                            let lines = readFrom fs
                            let splitRows = 
                                lines |> Seq.map (fun row ->
                                                    let lengths = findLengthArray row
                                                    Seq.unfold splitter (row, lengths) |> List.ofSeq) |> List.ofSeq

                            fs.Close()
                            splitRows)
            0
        | _ -> 1
    else
        let filenames = ["";"";""]
        let trees = filenames |> List.map parseFileIntoTree
        0