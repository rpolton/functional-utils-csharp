module Shaftesbury.Span.Program

open Shaftesbury.FSharp.Utils
open Shaftesbury.Span.XML.Parser
open Shaftesbury.Span.XML2.Parser
open Shaftesbury.Span.LCH.TextParser
open Shaftesbury.Span.HK.ExpandedFormat

let XMLfilenames = 
    [
        "ASXCLFEndOfDayRiskParameterFile130306.spn";
        "CFDEndOfDayRiskParameterFile120720.spn";
        "NZFEndOfDayRiskParameterFile130306.spn";
        "SFEEndOfDayRiskParameterFile130128.spn";
        "ASXCLFEndOfDayRiskParameterFile130319.spn";
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

// ftp://ftp.cmegroup.com/pub/span/data/
let CMEfilenames = 
    [
        "cme.20130228.a.pa2";
    ]
    |> List.map (fun nm -> @"C:\Users\Bob\development\data\Span\"+nm)

[<EntryPoint>]
let main args = 
    if Array.length args <> 0 then
        match args.[0] with
        | "XML" -> 
            let trees = XMLfilenames |> List.map prepareXMLFile |> List.map Shaftesbury.Span.XML.Parser.readXML
            0
        | "XML2" ->
            let trees = XMLfilenames |> List.map prepareXMLFile |> List.map Shaftesbury.Span.XML2.Parser.readXML
            0
        | "HK" ->
            let splitRows =
                HKfilenames |> 
                List.map (fun filename ->
                            use fs = new System.IO.StreamReader(filename)
                            let lines = readFrom fs
                            let splitRows = 
                                lines |> Seq.map (fun row ->
                                                    let lengths = Shaftesbury.Span.HK.ExpandedFormat.findLengthArray row
                                                    Seq.unfold splitter (row, lengths) |> List.ofSeq) |> List.ofSeq

                            fs.Close()
                            splitRows)
            let trees = splitRows |> List.map (fun recordSet -> recordSet |> List.map Shaftesbury.Span.HK.ExpandedFormat.convert)
            0
        | "CME" ->
            let splitRows =
                CMEfilenames |> 
                List.map (fun filename ->
                            use fs = new System.IO.StreamReader(filename)
                            let lines = readFrom fs
                            let splitRows = 
                                lines |> Seq.map (fun row ->
                                                    let lengths = Shaftesbury.Span.CME.ExpandedFormat.findLengthArray row
                                                    Seq.unfold splitter (row, lengths) |> List.ofSeq) |> List.ofSeq

                            fs.Close()
                            splitRows)
            let trees = splitRows |> List.map (fun recordSet -> recordSet |> List.map Shaftesbury.Span.CME.ExpandedFormat.convert)
            0
        | _ -> 1
    else
        let filenames = ["";"";""]
        let trees = filenames |> List.map parseFileIntoTree
        0