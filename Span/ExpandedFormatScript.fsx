#load "Utils.fs"
open Shaftesbury.FSharp.Utils

let row = @"0 HKEX  20130314II20843201303140843U2             C CUST"
let lengths = [2;6;8;1;2;4;8;4;2;1;1;5;6;1;1;5;]

let splitRow = Seq.unfold splitter (row, lengths) |> List.ofSeq


//// Load a file

let filenames = 
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
let filename = filenames.[4]

use fs = new System.IO.StreamReader(filename)

let lines = readFrom fs

#load "HK.ExpandedFormat.fs"
open Shaftesbury.Span.HK.ExpandedFormat

let splitRows = 
    lines |> Seq.map (fun row ->
                        let lengths = findLengthArray row
                        let splitRow = Seq.unfold splitter (row, lengths) |> List.ofSeq
                        splitRow) |> List.ofSeq

let records = splitRows |> List.map convert

fs.Close()
