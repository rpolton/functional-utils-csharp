namespace Shaftesbury.Span.LCH

module Span4RowTypes =
    open Shaftesbury.FSharp.Utils

    type LineType =
        | SPANHeader  
        | ContractTypeMapping
        | Currency
        | ExchangeRate
        | InterContractSpread
        | MarginGroup
        | ExchangeDetails
        | Scenario
        | CombinedContract
        | MonthTierDetails
        | LegSpreadDetails
        | PromptDateChargeDetails
        | IntercontractTierDetails
        | StrategySpreadDetails
        | ContractDetails
        | ContractExpiryDetails
        | RiskArray
        | Unknown

    type SPANHeader = 
        {
            FileType : string;
            FileFormat : int option;
            BusinessDate : string; // date
            FileIdentifier : string;
            CreationDate : string ; // date
            CreationTime : string ; // time
            NumberOfScenarios : int option;
        }

    let convertToSPANHeader (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,1);
            line.Substring (3,2);
            line.Substring (5,8);
            line.Substring (13,2);
            line.Substring (15,8);
            line.Substring (23,6);
            line.Substring (29);
            ]
        {
            FileType = parts.[1];
            FileFormat = parts.[2] |> toInt;
            BusinessDate = parts.[3];
            FileIdentifier = parts.[4];
            CreationDate = parts.[5];
            CreationTime = parts.[6];
            NumberOfScenarios = parts.[7] |> toInt;
        }

    type ContractTypeMapping = 
        {
            ContractType : string;
            GenericType : string;
            Description : string;
        }

    let convertToContractTypeMapping (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,2);
            line.Substring (4,1);
            line.Substring (5);
            ]
        {
            ContractType = parts.[1];
            GenericType = parts.[2];
            Description = parts.[3];
        }

    type Currency = 
        {
            CurrencyCode : string;
            Description : string;
            Exponent : string;
        }

    let convertToCurrency (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,21);
            line.Substring (26);
            ]
        {
            CurrencyCode = parts.[1];
            Description = parts.[2];
            Exponent = parts.[3];
        }

    type ExchangeRate = 
        {
            ContractCurrency : string;
            MarginCurrency : string;
            ExchangeRate : float option;
            ShiftUp : float option;
            ShiftDown : float option;
        }

    let convertToExchangeRate (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,3);
            line.Substring (8,10);
            line.Substring (18,6);
            line.Substring (24);
            ]
        {
            ContractCurrency = parts.[1];
            MarginCurrency = parts.[2];
            ExchangeRate = parts.[3] |> toFloat;
            ShiftUp = parts.[4] |> toFloat;
            ShiftDown = parts.[5] |> toFloat;
        }

    let toInterContractSpreadInternal = stringToListOfString 11

    type InterContractSpreadInternal = 
        {
        ExchangeCode : string;
        ContractCode : string;
        TierNumber : int option;
        SpreadSide : string;
        Delta_SpreadRatio : int option;
        }

    let convertToInterContractSpreadInternal (str:string) = 
        {
            ExchangeCode=str.Substring(0,3);
            ContractCode=str.Substring(3,3);
            TierNumber=str.Substring(6,2) |> toInt;
            SpreadSide=str.Substring(8,1);
            Delta_SpreadRatio=str.Substring(9,2) |> toInt;
        }

    type InterContractSpread = 
        {
            ContractGroup : string;
            SpreadPriority : int option;
            SpreadMethodCode : int option;
            CreditRate : float option;
            OffsetRate : float option;
            NumberOfLegs : int option;
            Details : InterContractSpreadInternal list; // technically only (and always) having 4 elements
        }

    let convertToInterContractSpread (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,3);
            line.Substring (8,2);
            line.Substring (10,6);
            line.Substring (16,7);
            line.Substring (23,2);
            line.Substring (25);
            ]
        {
            ContractGroup = parts.[1];
            SpreadPriority = parts.[2] |> toInt;
            SpreadMethodCode = parts.[3] |> toInt;
            CreditRate = parts.[4] |> toFloat;
            OffsetRate = parts.[5] |> toFloat;
            NumberOfLegs = parts.[6] |> toInt;
            Details = parts.[7] |> toInterContractSpreadInternal |> List.map convertToInterContractSpreadInternal;
        }

    type Scenario = 
        {
            ScenarioNumber : int option;
            ScenarioDescription : string;
            PairedScenarioNumber : int option;
        }

    let convertToScenario (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,11);
            line.Substring (16);
            ]
        {
            ScenarioNumber = parts.[1] |> toInt;
            ScenarioDescription = parts.[2];
            PairedScenarioNumber = parts.[3] |> toInt;
        }

    type MarginGroup = 
        {
            InitialMarginGroup : string;
            Description : string;
        }

    let convertToMarginGroup (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5);
            ]
        {
            InitialMarginGroup = parts.[1];
            Description = parts.[2];
        }

    type MonthTier =
        {
            TierNumber : int option;
            StartingExpiryGroup : string ; // this is a date
            EndingExpiryGroup : string ; // this is a date
        }

    let toMonthTiers (tiers:string) = 
        let rec toTier (tier:string) acc =
            match String.length tier with
            | GreaterThanOrEqualTo 18 true -> toTier (tier.Substring(18)) ({TierNumber = tier.Substring(0,2) |> toInt; StartingExpiryGroup = tier.Substring(2,8) ; EndingExpiryGroup = tier.Substring(10,8)} :: acc)
            | _ -> acc
        toTier tiers [] |> List.rev

    type MonthTierDetails =
        {
            NumberOfTiers : string;
            Tiers : MonthTier list;
        }

    let convertToMonthTierDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,2);
            line.Substring (4);
            ]
        {
            NumberOfTiers = parts.[1];
            Tiers = parts.[2] |> toMonthTiers;
        }

    type SpreadTier = 
        {
            TierNumber : int option;
            DeltaSpreadRatio : int option;
            MarketSide : string;
        }

    let toSpreadTiers (tiers:string) = 
        let rec toTier (tier:string) acc =
            match String.length tier with
            | GreaterThanOrEqualTo 5 true -> toTier (tier.Substring(5)) ({TierNumber = tier.Substring(0,2) |> toInt; DeltaSpreadRatio = tier.Substring(2,2) |> toInt ; MarketSide = tier.Substring(4,1)} :: acc)
            | _ -> acc
        toTier tiers [] |> List.rev

    type LegSpreadDetails =
        {
            InterpromptSpreadPriority : int option;
            SpreadChargeRate : int option;
            NumberOfLegs : int option;
            Tiers : SpreadTier list;
        }

    let convertToLegSpreadDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,10);
            line.Substring (15,2);
            line.Substring (17);
            ]
        {
            InterpromptSpreadPriority = parts.[1] |> toInt;
            SpreadChargeRate = parts.[2] |> toInt;
            NumberOfLegs = parts.[3] |> toInt;
            Tiers = parts.[4] |> toSpreadTiers;
        }

    type PromptDateChargeGroup =
        {
            ExpiryGroup : string; // date
            SpreadCharge : int option;
            OutrightCharge : int option;
            DeltaSign : string;
        }

    let toPromptDateChargeGroups (groups:string) = 
        let rec toGroup (group:string) acc =
            match String.length group with
            | GreaterThanOrEqualTo 29 true -> toGroup (group.Substring(29)) ({ExpiryGroup = group.Substring(0,8); SpreadCharge = group.Substring(8,10) |> toInt; OutrightCharge = group.Substring(18,10) |> toInt; DeltaSign = group.Substring(28,1)} :: acc)
            | _ -> acc
        toGroup groups [] |> List.rev

    type PromptDateChargeDetails =
        {
            NumberOfExpiryGroups : int option;
            Groups : PromptDateChargeGroup list;
        }

    let convertToPromptDateChargeDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,2);
            line.Substring (4);
            ]
        {
            NumberOfExpiryGroups = parts.[1] |> toInt;
            Groups = parts.[2] |> toPromptDateChargeGroups;
        }

    type IntercontractTier =
        {
            TierNumber : int option;
            StartingMonthTier : int option;
            EndingMonthTier : int option;
        }

    let toIntercontractTiers (tiers:string) = 
        let rec toTier (tier:string) acc =
            match String.length tier with
            | GreaterThanOrEqualTo 6 true -> toTier (tier.Substring(6)) ({TierNumber = tier.Substring(0,2) |> toInt; StartingMonthTier = tier.Substring(2,2) |> toInt; EndingMonthTier = tier.Substring(4,2) |> toInt} :: acc)
            | _ -> acc
        toTier tiers [] |> List.rev

    type IntercontractTierDetails =
        {
            NumberOfTiers : int option;
            Tiers : IntercontractTier list;
        }

    let convertToIntercontractTierDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,2);
            line.Substring (4);
            ]
        {
            NumberOfTiers = parts.[1] |> toInt;
            Tiers = parts.[2] |> toIntercontractTiers;
        }

    type StrategySpread = 
        {
            ExpiryGroup : string;
            DeltaSpreadRatio : int option;
            MarketSide : string;
        }

    let toStrategySpreads (tiers:string) = 
        let rec toTier (tier:string) acc =
            match String.length tier with
            | GreaterThanOrEqualTo 11 true -> toTier (tier.Substring(11)) ({ExpiryGroup = tier.Substring(0,8); DeltaSpreadRatio = tier.Substring(8,2) |> toInt ; MarketSide = tier.Substring(10,1)} :: acc)
            | _ -> acc
        toTier tiers [] |> List.rev

    type StrategySpreadDetails =
        {
            StrategySpreadPriority : int option;
            SpreadChargeRate : int option;
            NumberOfLegs : int option;
            Groups : StrategySpread list;
        }

    let convertToStrategySpreadDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,10);
            line.Substring (15,2);
            line.Substring (17);
            ]
        {
            StrategySpreadPriority = parts.[1] |> toInt;
            SpreadChargeRate = parts.[2] |> toInt;
            NumberOfLegs = parts.[3] |> toInt;
            Groups = parts.[4] |> toStrategySpreads;
        }

    type CombinedContract = 
        {
            CombinedContract : string;
            Description : string;
            ContractGroup : string;
            InitialMarginGroup : string;
            MarginCurrency : string;
            ExtremePriceShift : float option;
            LossCovered : float option;
            ShortOptMinimum : int option;
            StrategyMethod : int option;
            InterMonthMethod : int option;
            SpotMonthMethod : int option;
            EndOfRiskPeriod : string;
        }

    let convertToCombinedContract (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,20);
            line.Substring (25,3);
            line.Substring (28,3);
            line.Substring (31,3);
            line.Substring (34,6);
            line.Substring (40,12);
            line.Substring (52,2);
            line.Substring (54,2);
            line.Substring (56,2);
            line.Substring (58,2);
            line.Substring (60);
            ]
        {
            CombinedContract = parts.[1];
            Description = parts.[2];
            ContractGroup = parts.[3];
            InitialMarginGroup = parts.[4];
            MarginCurrency = parts.[5];
            ExtremePriceShift = parts.[6] |> toFloat;
            LossCovered = parts.[7] |> toFloat;
            ShortOptMinimum = parts.[8] |> toInt;
            StrategyMethod = parts.[9] |> toInt;
            InterMonthMethod = parts.[10] |> toInt;
            SpotMonthMethod = parts.[11] |> toInt;
            EndOfRiskPeriod = parts.[12];
        }

    type ExchangeDetails = 
        {
            ExchangeCode : string;
            ShortName : string;
            FileIdentifier : string;
        }

    let convertToExchangeDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,8);
            line.Substring (13);
            ]
        {
            ExchangeCode = parts.[1];
            ShortName = parts.[2];
            FileIdentifier = parts.[3];
        }

    type ContractDetails = 
        {
            ContractCode : string;
            ContractType : string;
            Description : string;
            Currency : string;
            TickDenominator : int option;
            MinPriceFluctuation : int option;
            TickValue : float option;
            DeltaDivisor : float option;
            DecimalLocator : int option;
            StrikeDenominator : int option;
            ScanningRange : int option;
            SettlementStyleMethod : int option;
        }

    let convertToContractDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,3);
            line.Substring (5,1);
            line.Substring (6,20);
            line.Substring (26,3);
            line.Substring (29,6);
            line.Substring (35,6);
            line.Substring (41,14);
            line.Substring (55,8);
            line.Substring (63,6);
            line.Substring (69,6);
            line.Substring (75,7);
            line.Substring (82);
            ]
        {
            ContractCode = parts.[1];
            ContractType = parts.[2];
            Description = parts.[3];
            Currency = parts.[4];
            TickDenominator = parts.[5] |> toInt;
            MinPriceFluctuation = parts.[6] |> toInt;
            TickValue = parts.[7] |> toFloat;
            DeltaDivisor = parts.[8] |> toFloat;
            DecimalLocator = parts.[9] |> toInt;
            StrikeDenominator = parts.[10] |> toInt;
            ScanningRange = parts.[11] |> toInt;
            SettlementStyleMethod = parts.[12] |> toInt;
        }

    let toExpiryGroups = stringToListOfString 8

    type ContractExpiryDetails =
        {
            ExpiryDate : string; // date
            DiscountFactor : float option;
            VolatilityShiftUp : float option;
            VolatilityShiftDown : float option;
            NumOfExpiryGroups : int option;
            ExpiryGroups : string list; // date, at least one must be present
        }

    let convertToContractExpiryDetails (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,8);
            line.Substring (10,8);
            line.Substring (18,6);
            line.Substring (24,6);
            line.Substring (30,3);
            line.Substring (33);
            ]
        {
            ExpiryDate = parts.[1];
            DiscountFactor = parts.[2] |> toFloat;
            VolatilityShiftUp = parts.[3] |> toFloat;
            VolatilityShiftDown = parts.[4] |> toFloat;
            NumOfExpiryGroups = parts.[5] |> toInt;
            ExpiryGroups = parts.[6] |> toExpiryGroups;
        }

    let toLossValues = stringToListOfString 7

    let fromTick value = (float value) / 32.0

    type RiskArray = 
        {
            StrikePrice : int option;
            ContractType : string;
            LotSize : int option;
            SettlementPrice : int option;
            CompositeDelta : float option;
            LossValue : (int option * float option) list;
        }
    let convertToRiskArray (line:string) =
        let parts = 
            [ 
            line.Substring (0,2);
            line.Substring (2,8);
            line.Substring (10,2);
            line.Substring (12,5);
            line.Substring (17,8);
            line.Substring (25,9);
            line.Substring (34);
            ]
        {
            StrikePrice = parts.[1] |> toInt;
            ContractType = parts.[2];
            LotSize = parts.[3] |> toInt;
            SettlementPrice = parts.[4] |> toInt;
            CompositeDelta = parts.[5] |> toFloat;
            LossValue = parts.[6] |> toLossValues |> List.map (fun s -> toInt s |>> (Identity, (applyToOption fromTick)));
        }
