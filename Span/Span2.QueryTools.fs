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

    // Here follow functions which can be used as the 'pattern' in 'findNode' or 'findNodeWithPath'

    let divNode f input =
        match input with
        | Node (SpanXMLDiv (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let raNode f input =
        match input with
        | Node (SpanXMLRa (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let divRateNode f input =
        match input with
        | Node (SpanXMLDivRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let undCNode f input =
        match input with
        | Node (SpanXMLUndC (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let intrRateNode f input =
        match input with
        | Node (SpanXMLIntrRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let optNode f input =
        match input with
        | Node (SpanXMLOpt (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let rateNode f input =
        match input with
        | Node (SpanXMLRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let scanRateNode f input =
        match input with
        | Node (SpanXMLScanRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let priceScanDefNode f input =
        match input with
        | Node (SpanXMLPriceScanDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let volScanDefNode f input =
        match input with
        | Node (SpanXMLVolScanDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let phyNode f input =
        match input with
        | Node (SpanXMLPhy (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let groupNode f input =
        match input with
        | Node (SpanXMLGroup (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let equityNode f input =
        match input with
        | Node (SpanXMLEquity (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let undPfNode f input =
        match input with
        | Node (SpanXMLUndPf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let futNode f input =
        match input with
        | Node (SpanXMLFut (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let seriesNode f input =
        match input with
        | Node (SpanXMLSeries (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let tierNode f input =
        match input with
        | Node (SpanXMLTier (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let tierWithRateNode f input =
        match input with
        | Node (SpanXMLTierWithRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let tierWithScanRateNode f input =
        match input with
        | Node (SpanXMLTierWithScanRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let tLegNode f input =
        match input with
        | Node (SpanXMLTLeg (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let pLegNode f input =
        match input with
        | Node (SpanXMLPLeg (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let sLegNode f input =
        match input with
        | Node (SpanXMLSLeg (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let scanPointDefNode f input =
        match input with
        | Node (SpanXMLScanPointDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let deltaPointDefNode f input =
        match input with
        | Node (SpanXMLDeltaPointDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let phyPfNode f input =
        match input with
        | Node (SpanXMLPhyPf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let equityPfNode f input =
        match input with
        | Node (SpanXMLEquityPf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let futPfNode f input =
        match input with
        | Node (SpanXMLFutPf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let oopPfNode f input =
        match input with
        | Node (SpanXMLOopPf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let oofPfNode f input =
        match input with
        | Node (SpanXMLOofPf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let ooePfNode f input =
        match input with
        | Node (SpanXMLOoePf (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let pfLinkNode f input =
        match input with
        | Node (SpanXMLPfLink (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let intraTiersNode f input =
        match input with
        | Node (SpanXMLIntraTiers (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let interTiersNode f input =
        match input with
        | Node (SpanXMLInterTiers (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let somTiersNode f input =
        match input with
        | Node (SpanXMLSomTiers (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let rateTiersNode f input =
        match input with
        | Node (SpanXMLRateTiers (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let dSpreadNode f input =
        match input with
        | Node (SpanXMLDSpread (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let hSpreadNode f input =
        match input with
        | Node (SpanXMLHSpread (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let spotRateNode f input =
        match input with
        | Node (SpanXMLSpotRate (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let curConvNode f input =
        match input with
        | Node (SpanXMLCurConv (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let pbRateDefNode f input =
        match input with
        | Node (SpanXMLPbRateDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let pointDefNode f input =
        match input with
        | Node (SpanXMLPointDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let exchangeNode f input =
        match input with
        | Node (SpanXMLExchange (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let ccDefNode f input =
        match input with
        | Node (SpanXMLCcDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let interSpreadsNode f input =
        match input with
        | Node (SpanXMLInterSpreads (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let currencyDefNode f input =
        match input with
        | Node (SpanXMLCurrencyDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let acctTypeDefNode f input =
        match input with
        | Node (SpanXMLAcctTypeDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let acctSubTypeDefNode f input =
        match input with
        | Node (SpanXMLAcctSubTypeDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let groupTypeDefNode f input =
        match input with
        | Node (SpanXMLGroupTypeDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let groupDefNode f input =
        match input with
        | Node (SpanXMLGroupDef (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let clearingOrgNode f input =
        match input with
        | Node (SpanXMLClearingOrg (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let definitionNode f input =
        match input with
        | Node (SpanXMLDefinition (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None

    let pointInTimeNode f input =
        match input with
        | Node (SpanXMLPointInTime (record) as uNode, _) as node when f record -> Some(uNode,node)
        | _ -> None
