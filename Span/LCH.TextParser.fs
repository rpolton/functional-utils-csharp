namespace Shaftesbury.Span.LCH

module TextParser =
    open Shaftesbury.FSharp.Utils
    open Shaftesbury.Span.LCH.Span4RowTypes
    open Shaftesbury.Span.LCH.ParserDataTypes

    let classify = function
        | FirstNChars 2 "10" as line -> SPANHeader
        | FirstNChars 2 "11" as line -> ContractTypeMapping
        | FirstNChars 2 "12" as line -> Currency
        | FirstNChars 2 "13" as line -> ExchangeRate
        | FirstNChars 2 "14" as line -> InterContractSpread
        | FirstNChars 2 "15" as line -> Scenario
        | FirstNChars 2 "16" as line -> MarginGroup
        | FirstNChars 2 "20" as line -> ExchangeDetails
        | FirstNChars 2 "30" as line -> CombinedContract
        | FirstNChars 2 "31" as line -> MonthTierDetails
        | FirstNChars 2 "32" as line -> LegSpreadDetails
        | FirstNChars 2 "33" as line -> PromptDateChargeDetails
        | FirstNChars 2 "34" as line -> IntercontractTierDetails
        | FirstNChars 2 "35" as line -> StrategySpreadDetails
        | FirstNChars 2 "40" as line -> ContractDetails
        | FirstNChars 2 "50" as line -> ContractExpiryDetails
        | FirstNChars 2 "60" as line -> RiskArray
        | _ -> Unknown

    // Evil Imperative Code Follows. This needs rewriting ...
    let buildContractDetailsTree len counter' (lines:(string*LineType) list) =
        let mutable counter = counter'
        let mutable contractDetailsTree : ContractDetailsTree list = []
        while counter < len && lines.[counter] |> second = ContractDetails do
            let contractDetails = convertToContractDetails (lines.[counter] |> first)
            let mutable contractExpiryDetailsTree : ContractExpiryDetailsTree list = []
            counter <- counter + 1
            while counter < len && lines.[counter] |> second = ContractExpiryDetails do
                let contractExpiryDetails = convertToContractExpiryDetails (lines.[counter] |> first)
                let mutable riskArrayList : RiskArray list = []
                counter <- counter + 1
                while counter < len && lines.[counter] |> second = RiskArray do
                    riskArrayList <- convertToRiskArray (lines.[counter] |> first) :: riskArrayList
                    counter <- counter + 1
                contractExpiryDetailsTree <- {ContractExpiryDetails=contractExpiryDetails; SeriesDetails=riskArrayList} :: contractExpiryDetailsTree
            contractDetailsTree <- {ContractDetails=contractDetails;Expiries=contractExpiryDetailsTree} :: contractDetailsTree
        contractDetailsTree, counter

    let buildCombinedContractDetailsTree len counter' (lines:(string*LineType) list) =
        let mutable counter = counter'
        let combinedContractDetails = convertToCombinedContract (lines.[counter] |> first)
        let mutable contractDetailsTree : ContractDetailsTree list = []
        let mutable monthTierDetails : MonthTierDetails list = []
        let mutable legSpreadDetails : LegSpreadDetails list = []
        let mutable promptDateChargeDetails : PromptDateChargeDetails list = []
        let mutable intercontractTierDetails : IntercontractTierDetails list = []
        let mutable strategySpreadDetails : StrategySpreadDetails list = []
        counter <- counter + 1
        if counter < len then
            let mutable line = lines.[counter] |> first
            let mutable cl = lines.[counter] |> second
            while cl = MonthTierDetails || cl = LegSpreadDetails || cl = PromptDateChargeDetails || cl = IntercontractTierDetails || cl = StrategySpreadDetails do
                if cl = MonthTierDetails then monthTierDetails <- convertToMonthTierDetails line :: monthTierDetails
                if cl = LegSpreadDetails then legSpreadDetails <- convertToLegSpreadDetails line :: legSpreadDetails
                if cl = PromptDateChargeDetails then promptDateChargeDetails <- convertToPromptDateChargeDetails line :: promptDateChargeDetails
                if cl = IntercontractTierDetails then intercontractTierDetails <- convertToIntercontractTierDetails line :: intercontractTierDetails
                if cl = StrategySpreadDetails then strategySpreadDetails <- convertToStrategySpreadDetails line :: strategySpreadDetails
                counter <- counter + 1
                if counter < len then 
                    line <- lines.[counter] |> first
                    cl <- lines.[counter] |> second
            let lst,ct = buildContractDetailsTree len counter lines
            contractDetailsTree <- lst// :: contractDetailsTree
            counter <- ct
        {
            CombinedContractDetails = combinedContractDetails; 
            MonthTierDetails = monthTierDetails;
            LegSpreadDetails = legSpreadDetails;
            PromptDateChargeDetails = promptDateChargeDetails;
            IntercontractTierDetails = intercontractTierDetails;
            StrategySpreadDetails = strategySpreadDetails;
            Contracts = contractDetailsTree
        }, counter

    let buildCombinedContractDetails len counter' (lines:(string*LineType) list) =
        let mutable counter = counter'
        let mutable combinedContractDetailsTreeList : CombinedContractDetailsTree list = []
        while counter < len && lines.[counter] |> second = CombinedContract do
            let lst, c = buildCombinedContractDetailsTree len counter lines
            combinedContractDetailsTreeList <- lst :: combinedContractDetailsTreeList
            counter <- c
        combinedContractDetailsTreeList, counter

    let buildExchangeDetailsTree lines = 
        let mutable treeList : ExchangeDetailsTree list = []
        let mutable counter = 0
        let len = List.length lines
        while counter < len do
            let line,cl = lines.[counter]
            if cl = ExchangeDetails then
                let exchangeDetails = convertToExchangeDetails line
                let combinedContractDetailsTreeList, ct = buildCombinedContractDetails len (counter+1) lines
                counter <- ct
                treeList <- {ExchangeDetails = exchangeDetails; CombinedContractDetails = combinedContractDetailsTreeList} :: treeList
            else counter <- counter + 1
        treeList

    let buildCommonData lines =
        let header = lines |> Seq.find (fun (line,cl) -> cl = SPANHeader) |> first |> convertToSPANHeader
        let contractTypeMap = lines |> Seq.filter (fun (line,cl) -> cl = ContractTypeMapping) |> Seq.map (first >> convertToContractTypeMapping) |> Seq.toList
        let currency = lines |> Seq.filter (fun (line,cl) -> cl = Currency) |> Seq.map (first >> convertToCurrency) |> Seq.toList
        let exchangeRates = lines |> Seq.filter (fun (line,cl) -> cl = ExchangeRate) |> Seq.map (first >> convertToExchangeRate) |> Seq.toList
        let spreads = lines |> Seq.filter (fun (line,cl) -> cl = InterContractSpread) |> Seq.map (first >> convertToInterContractSpread) |> Seq.toList
        let scenarios = lines |> Seq.filter (fun (line,cl) -> cl = Scenario) |> Seq.map (first >> convertToScenario) |> Seq.toList
        let marginGroups = lines |> Seq.filter (fun (line,cl) -> cl = MarginGroup) |> Seq.map (first >> convertToMarginGroup) |> Seq.toList
        {Header = header; ContractTypeMap = contractTypeMap; Currency=currency;ExchangeRates=exchangeRates;Spreads=spreads;Scenarios=scenarios;MarginGroups=marginGroups;}

    let extractSpanTree lines =
        // according to the file format, the Common Data elements are only at the top of the file
        let headerLines, rest = 
            lines |> 
            Seq.toList |>
            List.partition (fun (line,cl) -> List.exists (fun elem -> elem = cl ) [SPANHeader;ContractTypeMapping;Currency;ExchangeRate;InterContractSpread;Scenario;MarginGroup])
        buildCommonData headerLines, buildExchangeDetailsTree rest

    let parseFileIntoTree (filename:string) =
        use fs = new System.IO.StreamReader(filename)
        let lines = readFrom fs
        let spanTree = lines |> Seq.map (fun line -> line, classify line) |> extractSpanTree
        fs.Close()
        spanTree
