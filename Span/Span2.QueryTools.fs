namespace Shaftesbury.Span.XML2

module QueryTools =
    open Shaftesbury.FSharp.Utils
    open Shaftesbury.Span.XML2.ParserDataTypes

    // Tools for the XML SPAN 2 parser
    let findDivsWithPath f tree =
        let rec findDivs tree acc path =
            match tree with
            | Node (SpanXMLDiv (div) as uNode, _) as node when f div -> (node, (uNode :: path |> List.rev)) :: acc
            | Node (_, []) -> acc
            | Node (uNode, trees) -> trees |> List.map (fun node -> findDivs node acc (uNode :: path)) |> List.concat
        findDivs tree [] []

    let findDivs f tree = findDivsWithPath f tree |> List.map first

    //let fn (div:SpanXMLDiv) = between 20100325 20130325 div.SetlDate
    //findDivs fn trees.[0]

    //let divNode f input =
    //    match input with
    //    | Node (SpanXMLDiv (div) as uNode, _) as node when f div -> Some(uNode,node)
    //    | _ -> None

    let findNodeWithPath pattern tree =
        let rec findNode tree acc path =
            match pattern tree with
            | Some(uNode,node) -> (node, (uNode :: path |> List.rev)) :: acc
            | _ ->
                match tree with
                | Node (_, []) -> acc
                | Node (uNode, trees) -> trees |> List.map (fun node -> findNode node acc (uNode :: path)) |> List.concat
        findNode tree [] []

    //findNodeWithPath (divNode fn) trees.[0]

    let findNode pattern tree = findNodeWithPath pattern tree |> List.map first