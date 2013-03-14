module Shaftesbury.Span.LCH.QueryTools
open Shaftesbury.Span.LCH.ParserDataTypes
open Shaftesbury.Span.LCH.Span4RowTypes
open Shaftesbury.FSharp.Utils

// Tools for the fixed-format SPAN file
// Find all the InterContractSpread objects which match the two pairs of exchange and contract codes
let getInterContractSpreadDetails (((commonData:CommonData),exchDetailsTreeList) as tree) (exchCode1,contCode1) (exchCode2,contCode2) =
    let matches exchCode contCode (detail:InterContractSpreadInternal) = detail.ExchangeCode.Trim()=exchCode && detail.ContractCode.Trim()=contCode
    commonData.Spreads |> 
        List.filter (fun spread -> 
        spread.Details |> List.exists (matches exchCode1 contCode1) &&
        spread.Details |> List.exists (matches exchCode2 contCode2))

// Find, in SpreadPriority order, all InterContractSpread objects which can be applied to the portfolio, where the portfolio is a list of exchange and contract code pairs
let getAllInterContractSpreadsForPortfolio (((commonData:CommonData),exchDetailsTreeList) as tree) (portfolio: (string*string) list) =
    let orderedSpreads = commonData.Spreads |> List.sortBy (fun spread -> spread.SpreadPriority)
    let getCodes (detail:InterContractSpreadInternal) = detail.ExchangeCode.Trim(), detail.ContractCode.Trim()
    let getCodesForSpread (spread:InterContractSpread) = spread.Details |> List.map getCodes
    let doIt ((pfCodes,discoveredSpreads) as state) spreadToTest =
        let interContractSpreadCodes = getCodesForSpread spreadToTest |> Set.ofList
        if Set.isSubset interContractSpreadCodes pfCodes then // if all the codes associated with this InterContractSpread object are in the portfolio then
            (Set.difference pfCodes interContractSpreadCodes), (spreadToTest :: discoveredSpreads) // remove the interContractSpreadCodes from the portfolio codes
        else
            pfCodes, discoveredSpreads
    orderedSpreads |> List.fold doIt (Set.ofList portfolio, []) |> second
