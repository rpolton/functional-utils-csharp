namespace Shaftesbury.Span.LCH

module ExpandedFormat =
    open Shaftesbury.FSharp.Utils

    let LCHformat_lengths =
        [
            "10", [2;1;2;8;2;8;6;3;];
            "11", [2;2;1;20;];
            "12", [2;3;20;2;];
            "13", [2;3;3;10;6;6;];
            "14", [2;3;3;2;6;7;2;3;3;2;1;2;3;3;2;1;2;3;3;2;1;2;3;3;2;1;2;];
            "15", [2;3;15;3;];
            "16", [2;3;25;];
            "20", [2;3;8;2;];
            "30", [2;3;20;3;3;3;4;6;10;2;2;2;8;];
            "31", [2;2;2;8;8;2;8;8;2;8;8;2;8;8;2;8;8;2;8;8;2;8;8;2;8;8;];
            "32", [2;3;10;2;2;2;1;2;2;1;2;2;1;2;2;1;];
            "33", [2;2;8;10;10;1;8;10;10;1;8;10;10;1;8;10;10;1;];
            "34", [2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;2;];
            "35", [2;3;10;2;8;2;1;8;2;1;8;2;1;8;2;1;8;2;1;8;2;1;8;2;1;8;2;1;];
            "40", [2;3;1;20;3;6;6;14;8;6;6;7;1;];
            "50", [2;8;8;6;6;3;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;8;];
            "60", [2;8;2;5;8;9;7;7;7;7;7;7;7;7;7;7;7;7;7;7;7;7;];
        ] |> toDictionary first second

    let findLengthArray (line:string) =
        let id = line.Substring (0,2)
        LCHformat_lengths.[id]

    type SPANHeader =
        {
            RecordType : string;
            FileType : string;
            FormatVersion : string;
            BusinessDate : string;
            FileIdentifier : string;
            CreationDate : string;
            CreationTime : string;
            NumberOfScenarios : string;
        }

    let convertToSPANHeader (fields: string list) =
        {
            RecordType = fields.[0];
            FileType = fields.[1];
            FormatVersion = fields.[2];
            BusinessDate = fields.[3];
            FileIdentifier = fields.[4];
            CreationDate = fields.[5];
            CreationTime = fields.[6];
            NumberOfScenarios = fields.[7];
        }

    type ContractTypeMapping =
        {
            RecordType : string;
            ContractType : string;
            GenericContractType : string;
            ContractTypeDescription : string;
        }

    let convertToContractTypeMapping (fields : string list) =
        {
            RecordType = fields.[0];
            ContractType = fields.[1];
            GenericContractType = fields.[2];
            ContractTypeDescription = fields.[3];
        }

    type CurrencyDetails = 
        {
            RecordType : string;
            CurrencyCode : string;
            CurrencyDescription : string;
            CurrencyExponent : string;
        }

    let convertToCurrencyDetails (fields : string list) =
        {
            RecordType = fields.[0];
            CurrencyCode = fields.[1];
            CurrencyDescription = fields.[2];
            CurrencyExponent = fields.[3];
        }

    type CurrencyConversionDetails =
        {
            RecordType : string;
            ContractCurrency : string;
            MarginCurrency : string;
            ContractMarginCurrencyMultiplier : string;
            PercentageFXShiftUp : string;
            PercentageFXShiftDown : string;
        }

    let convertToCurrencyConversionDetails (fields : string list) =
        {
            RecordType = fields.[0];
            ContractCurrency = fields.[1];
            MarginCurrency = fields.[2];
            ContractMarginCurrencyMultiplier = fields.[3];
            PercentageFXShiftUp = fields.[4];
            PercentageFXShiftDown = fields.[5];
        }

    type InterContractSpread' = 
        {
            ExchangeCode : string;
            ContractCode : string;
            TierNumber : int option;
            SpreadSide : string;
            Delta_SpreadRatio : int option;
        }

    let private convertToInterContractSpread' (fields:string list) = 
        {
            ExchangeCode=fields.[0];
            ContractCode=fields.[1];
            TierNumber=fields.[2] |> toInt;
            SpreadSide=fields.[3];
            Delta_SpreadRatio=fields.[4] |> toInt;
        }

    type InterContractSpread = 
        {
            RecordType : string;
            ContractGroup : string;
            SpreadPriority : int option;
            SpreadMethodCode : int option;
            CreditRate : float option;
            OffsetRate : float option;
            NumberOfLegs : int option;
            Details : InterContractSpread' list;
        }

    let convertToInterContractSpread (fields:string list) =
        {
            RecordType = fields.[0];
            ContractGroup = fields.[1];
            SpreadPriority = fields.[2] |> toInt;
            SpreadMethodCode = fields.[3] |> toInt;
            CreditRate = fields.[4] |> toFloat;
            OffsetRate = fields.[5] |> toFloat;
            NumberOfLegs = fields.[6] |> toInt;
            Details = Seq.skip 7 fields |> List.ofSeq |> groupInto 5 |> List.map convertToInterContractSpread';
        }

    type ScenarioDescription =
        {
            RecordType : string;
            ScenarioNumber : int option;
            ScenarioDescription : string;
            PairedScenarioNumber : int option;
        }

    let convertToScenarioDescription (fields : string list) =
        {
            RecordType = fields.[0];
            ScenarioNumber = fields.[1] |> toInt;
            ScenarioDescription = fields.[2];
            PairedScenarioNumber = fields.[3] |> toInt;
        }

    type MarginGroupDescription =
        {
            RecordType : string;
            InitialMarginGroup : string;
            InitialMarginGroupDescription : string;
        }

    let convertToMarginGroupDescription (fields : string list) =
        {
            RecordType = fields.[0];
            InitialMarginGroup = fields.[1];
            InitialMarginGroupDescription = fields.[2];
        }

    type ExchangeDetails =
        {
            RecordType : string;
            ExchangeCode : string;
            ExchangeShortName : string;
            FileIdentifier : string;
        }

    let convertToExchangeDetails (fields : string list) =
        {
            RecordType = fields.[0];
            ExchangeCode = fields.[1];
            ExchangeShortName = fields.[2];
            FileIdentifier = fields.[3];
        }

    type CombinedContractDetails =
        {
            RecordType : string;
            CombinedContractCode : string;
            CombinedContractName : string;
            ContractGroup : string;
            InitialMarginGroup : string;
            MarginCurrencyCode : string;
            ExtremePriceShift : float option;
            LossCovered : float option;
            ShortOptionMinimumChargeRate : int option;
            StrategySpreadMethodCode : int option;
            IntermonthSpreadMethodCode : int option; // or InterpromptSpreadMethodCode
            SpotMonthMethodCode : int option; // or PromptDateMethodCode
            EndOfRiskPeriod : string;
        }

    let convertToCombinedContractDetails (fields : string list) =
        {
            RecordType = fields.[0];
            CombinedContractCode = fields.[1];
            CombinedContractName = fields.[2];
            ContractGroup = fields.[3];
            InitialMarginGroup = fields.[4];
            MarginCurrencyCode = fields.[5];
            ExtremePriceShift = fields.[6] |> toFloat;
            LossCovered = fields.[7] |> toFloat;
            ShortOptionMinimumChargeRate = fields.[8] |> toInt;
            StrategySpreadMethodCode = fields.[9] |> toInt;
            IntermonthSpreadMethodCode = fields.[10] |> toInt;
            SpotMonthMethodCode = fields.[11] |> toInt;
            EndOfRiskPeriod = fields.[12];
        }

    type Tier' =
        {
            TierNumber : int option;
            StartingExpiryGroup : string;
            EndingExpiryGroup : string;
        }

    let private convertToTier' (fields : string list) =
        {
            TierNumber = fields.[0] |> toInt;
            StartingExpiryGroup = fields.[1];
            EndingExpiryGroup = fields.[2];
        }

    type MonthTierDetails =
        {
            RecordType : string;
            NumberOfTiers : int option;
            Tiers : Tier' list;
        }

    let convertToMonthTierDetails (fields : string list) =
        {
            RecordType = fields.[0];
            NumberOfTiers = fields.[1] |> toInt;
            Tiers = fields |> Seq.skip 2 |> List.ofSeq |> groupInto 3 |> List.map convertToTier';
        }

    type LegSpread' =
        {
            TierNumber : int option;
            DeltaSpreadRatio : int option;
            MarketSide : string;
        }

    let private convertToLegSpread' (fields : string list) =
        {
            TierNumber = fields.[0] |> toInt;
            DeltaSpreadRatio = fields.[1] |> toInt;
            MarketSide = fields.[2];
        }

    type LegSpreadDetails =
        {
            RecordType : string;
            IntermonthSpreadPriority : int option;
            SpreadChargeRate : int option;
            NumberOfLegs : int option;
            Tiers : LegSpread' list;
        }

    let convertToLegSpreadDetails (fields : string list) =
        {
            RecordType = fields.[0];
            IntermonthSpreadPriority = fields.[1] |> toInt;
            SpreadChargeRate = fields.[2] |> toInt;
            NumberOfLegs = fields.[3] |> toInt;
            Tiers = fields |> Seq.skip 4 |> List.ofSeq |> groupInto 3 |> List.map convertToLegSpread';
        }

    type SpotMonth' =
        {
            SpotMonth : string;
            SpreadCharge : int option;
            OutrightCharge : int option;
            DeltaSign : string;
        }

    let private convertToSpotMonth' (fields : string list) =
        {
            SpotMonth = fields.[0];
            SpreadCharge = fields.[1] |> toInt;
            OutrightCharge = fields.[2] |> toInt;
            DeltaSign = fields.[3];
        }

    type SpotMonthChargeDetails =
        {
            RecordType : string;
            NumberOfSpotMonths : int option;
            SpotMonths : SpotMonth' list;
        }

    let convertToSpotMonthChargeDetails (fields : string list) =
        {
            RecordType = fields.[0];
            NumberOfSpotMonths = fields.[1] |> toInt;
            SpotMonths = fields |> Seq.skip 2 |> List.ofSeq |> groupInto 4 |> List.map convertToSpotMonth';
        }

    type InterContractTier' =
        {
            TierNumber : int option;
            StartingMonthTierNumber : int option;
            EndingMonthTierNumber : int option;
        }

    let private convertToInterContractTier' (fields : string list) =
        {
            TierNumber = fields.[0] |> toInt;
            StartingMonthTierNumber = fields.[1] |> toInt;
            EndingMonthTierNumber = fields.[2] |> toInt;
        }

    type InterContractTierDetails =
        {
            RecordType : string;
            NumberOfTiers : int option;
            InterContractTiers : InterContractTier' list;
        }

    let convertToInterContractTierDetails (fields : string list) =
        {
            RecordType = fields.[0];
            NumberOfTiers = fields.[1] |> toInt;
            InterContractTiers = fields |> Seq.skip 2 |> List.ofSeq |> groupInto 3 |> List.map convertToInterContractTier';
        }

    type StrategySpread' =
        {
            ExpiryGroup : string;
            DeltaSpreadRatio : int option;
            MarketSide : string;
        }

    let private convertToStrategySpread' (fields : string list) =
        {
            ExpiryGroup = fields.[0];
            DeltaSpreadRatio = fields.[1] |> toInt;
            MarketSide = fields.[2];
        }

    type StrategySpreadDetails =
        {
            RecordType : string;
            StrategySpreadPriority : int option;
            SpreadChargeRate : int option;
            NumberOfLegs : int option;
            StrategySpreads : StrategySpread' list;
        }

    let convertToStrategySpreadDetails (fields : string list) =
        {
            RecordType = fields.[0];
            StrategySpreadPriority = fields.[1] |> toInt;
            SpreadChargeRate = fields.[2] |> toInt;
            NumberOfLegs = fields.[3] |> toInt;
            StrategySpreads = fields |> Seq.skip 4 |> List.ofSeq |> groupInto 3 |> List.map convertToStrategySpread';
        }

    type ContractDetails =
        {
            RecordType : string;
            ContractCode : string;
            GenericContractType : string;
            ContractDescription : string;
            ContractCurrency : string;
            TickDenominator : int option;
            MinimumPriceFluctuation : int option;
            TickValue : float option;
            DeltaDivisor : float option;
            DecimalLocator : int option;
            StrikeDenominator : int option;
            ScanningRange : int option;
            SettlementStyleMethod : int option;
        }

    let convertToContractDetails (fields : string list) =
        {
            RecordType = fields.[0];
            ContractCode = fields.[1];
            GenericContractType = fields.[2];
            ContractDescription = fields.[3];
            ContractCurrency = fields.[4];
            TickDenominator = fields.[5] |> toInt;
            MinimumPriceFluctuation = fields.[6] |> toInt;
            TickValue = fields.[7] |> toFloat;
            DeltaDivisor = fields.[8] |> toFloat;
            DecimalLocator = fields.[9] |> toInt;
            StrikeDenominator = fields.[10] |> toInt;
            ScanningRange = fields.[11] |> toInt;
            SettlementStyleMethod = fields.[12] |> toInt;
        }

    type ContractExpiryDetails =
        {
            RecordType : string;
            ExpiryDate : string;
            DiscountFactor : float option;
            VolatilityShiftUp : float option;
            VolatilityShiftDown : float option;
            NumberOfExpiryGroups : int option;
            ExpiryGroups : string list;
        }

    let convertToContractExpiryDetails (fields : string list) =
        {
            RecordType = fields.[0];
            ExpiryDate = fields.[1];
            DiscountFactor = fields.[2] |> toFloat;
            VolatilityShiftUp = fields.[3] |> toFloat;
            VolatilityShiftDown = fields.[4] |> toFloat;
            NumberOfExpiryGroups = fields.[5] |> toInt;
            ExpiryGroups = fields |> Seq.skip 6 |> List.ofSeq;
        }

    type SeriesDetails =
        {
            RecordType : string;
            StrikePrice : int option;
            ContractType : string;
            LotSize : int option;
            SettlementPrice : int option;
            CompositeDelta : float option;
            LossValues : int option list;
        }

    let convertToSeriesDetails (fields : string list) =
        {
            RecordType = fields.[0];
            StrikePrice = fields.[1] |> toInt;
            ContractType = fields.[2];
            LotSize = fields.[3] |> toInt;
            SettlementPrice = fields.[4] |> toInt;
            CompositeDelta = fields.[5] |> toFloat;
            LossValues = fields |> Seq.skip 6 |> List.ofSeq |> List.map toInt;
        }

    type LineType =
        | SPANHeader of SPANHeader
        | ContractTypeMapping of ContractTypeMapping
        | CurrencyDetails of CurrencyDetails
        | CurrencyConversionDetails of CurrencyConversionDetails
        | InterContractSpread of InterContractSpread
        | ScenarioDescription of ScenarioDescription
        | MarginGroupDescription of MarginGroupDescription
        | ExchangeDetails of ExchangeDetails
        | CombinedContractDetails of CombinedContractDetails
        | MonthTierDetails of MonthTierDetails
        | LegSpreadDetails of LegSpreadDetails
        | SpotMonthChargeDetails of SpotMonthChargeDetails
        | InterContractTierDetails of InterContractTierDetails
        | StrategySpreadDetails of StrategySpreadDetails
        | ContractDetails of ContractDetails
        | ContractExpiryDetails of ContractExpiryDetails
        | SeriesDetails of SeriesDetails
        | Unknown of string

    let convert (fields:string list) =
        match fields.[0].Trim() with
        | "10" -> SPANHeader (convertToSPANHeader fields)
        | "11" -> ContractTypeMapping (convertToContractTypeMapping fields)
        | "12" -> CurrencyDetails (convertToCurrencyDetails fields)
        | "13" -> CurrencyConversionDetails (convertToCurrencyConversionDetails fields)
        | "14" -> InterContractSpread (convertToInterContractSpread fields)
        | "15" -> ScenarioDescription (convertToScenarioDescription fields)
        | "16" -> MarginGroupDescription (convertToMarginGroupDescription fields)
        | "20" -> ExchangeDetails (convertToExchangeDetails fields)
        | "30" -> CombinedContractDetails (convertToCombinedContractDetails fields)
        | "31" -> MonthTierDetails (convertToMonthTierDetails fields)
        | "32" -> LegSpreadDetails (convertToLegSpreadDetails fields)
        | "33" -> SpotMonthChargeDetails (convertToSpotMonthChargeDetails fields)
        | "34" -> InterContractTierDetails (convertToInterContractTierDetails fields)
        | "35" -> StrategySpreadDetails (convertToStrategySpreadDetails fields)
        | "40" -> ContractDetails (convertToContractDetails fields)
        | "50" -> ContractExpiryDetails (convertToContractExpiryDetails fields)
        | "60" -> SeriesDetails (convertToSeriesDetails fields)
        | _ as str -> Unknown (str)

    type tree =
        | Node of LineType * tree list

    let buildTree records =
        let buildL6 recs =
            let rec build l acc =
                match l with 
                | SeriesDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | _ -> l, acc
            build recs []

        let buildL5 recs =
            let rec build l acc =
                match l with 
                | ContractExpiryDetails(_) as record :: tl -> 
                    let remainder, l6 = buildL6 tl
                    build remainder (Node (record, l6) :: acc)
                | _ -> l, acc
            build recs []

        let buildL4 recs =
            let rec build l acc =
                match l with 
                | ContractDetails(_) as record :: tl -> 
                    let remainder, l5 = buildL5 tl
                    build remainder (Node (record, l5) :: acc)
                | MonthTierDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | LegSpreadDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | SpotMonthChargeDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | InterContractTierDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | StrategySpreadDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | _ -> l, acc
            build recs []

        let buildL3 recs =
            let rec build l acc =
                match l with 
                | CombinedContractDetails(_) as record :: tl -> 
                    let remainder, l4 = buildL4 tl
                    build remainder (Node (record, l4) :: acc)
                | _ -> l, acc
            build recs []

        let buildL2 recs =
            let rec build l acc =
                match l with 
                | ExchangeDetails(_) as record :: tl -> 
                    let remainder, l3 = buildL3 tl
                    build remainder (Node (record, l3) :: acc)
                | ContractTypeMapping(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | CurrencyDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | CurrencyConversionDetails(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | InterContractSpread(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | ScenarioDescription(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | MarginGroupDescription(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | _ -> l, acc
            build recs []

        match records with
        | SPANHeader(_) as record :: tl ->
            let remainder, l2 = buildL2 tl
            remainder, Node (record, l2)
