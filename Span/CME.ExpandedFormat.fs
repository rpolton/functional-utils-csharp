namespace Shaftesbury.Span.CME

module ExpandedFormat =
    open Shaftesbury.FSharp.Utils

    // This parser corresponds to the CME Expanded Format
    // as described http://cme-ch.com/span/spanl300.htm

    let CMEformat_lengths =
        [
            "0", [2;6;8;1;2;4;8;4;2;1;1;5;6;1;1;5;1;1;1;5;1;1;1;5;1;1;1;5;51;];
            "1", [2;3;2;2;123;];
            "2", [2;3;1;6;1;3;1;1;1;1;2;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;10;3;1;1;15;]; // the same as HK
            "3", [2;6;2;2;6;6;2;6;6;2;6;6;2;6;6;2;4;4;4;2;2;2;2;2;2;2;2;36;];
            "4", [2;6;2;52;7;3;3;3;1;53;];  // the 52 breaks down into 2;2;6;7;7;2;6;7;7;6; or 10;7;35;
            "5", [2;3;7;6;6;6;6;6;6;6;6;6;6;60;];
            "6", [2;3;4;7;3;1;6;7;1;3;1;6;7;1;3;1;6;7;1;3;1;6;7;1;2;3;1;6;1;2;2;2;2;1;7;4;1;7;7;7;7;1;];
            "81", [2;3;10;10;3;1;6;2;1;6;2;1;7;5;1;5;1;5;1;5;1;5;1;5;1;5;1;5;1;5;1;14;1;];
            "82", [2;3;10;10;3;1;6;2;1;6;2;1;7;5;1;5;1;5;1;5;1;5;1;5;1;5;1;5;1;8;7;1;1;5;1;1;7;1;2;1;14;2;1;14;2;1;];
            "83", [2;3;10;10;3;1;6;2;1;6;2;1;7;8;1;8;1;8;1;8;1;8;1;8;1;8;1;8;1;8;1;];
            "84", [2;3;10;10;3;1;6;2;1;6;2;1;7;8;1;8;1;8;1;8;1;8;1;8;1;8;1;5;1;8;7;1;1;5;1;1;7;1;2;1;14;2;1;14;2;1;];
            "91", [2;3;2;10;6;3;3;2;15;3;1;8;5;9;60];
            "92", [2;3;2;10;6;3;3;2;15;50;10;26;];
            "B", [2;3;10;3;6;2;1;6;2;1;8;8;5;5;5;5;7;6;6;8;10;2;8;1;7;1;14;2;1;2;1;2;1;12;1;1;2;1;];
            "C", [2;6;2;2;2;7;2;2;2;1;2;2;2;1;2;2;2;1;2;2;2;1;2;2;2;1;2;2;2;1;2;2;2;1;2;2;2;1;55;];
            "E", [2;6;5;7;4;3;6;1;4;3;6;1;4;3;6;1;4;3;6;1;4;];
            "P", [2;3;10;3;15;3;3;1;1;14;8;2;3;1;3;1;2;4;35;1;1;5;5;5;5;2;5;14;2;1;1;];
            "R", [2;3;10;6;3;10;6;90;];
            "S", [2;6;2;2;2;6;6;2;6;6;2;6;6;2;6;6;2;6;6;1;2;2;2;2;2;2;2;2;2;2;2;7;7;7;7;7;];
            "T", [2;3;1;3;1;10;];
            "V", [1;3;10;6;2;8;13;1;1;13;1;1;1;3;3;1;3;3;1;3;3;6;44;];
            "X", [2;1;10;7;];
            "Y", [2;10;10;];
            "Z", [2;3;10;5;6;2;7;3;1;3;10;3;6;2;4;1;2;7;1;];
        ] |> toDictionary first second

    let findLengthArray = function
        | FirstNChars 2 "81" as line -> CMEformat_lengths.["81"]
        | FirstNChars 2 "82" as line -> CMEformat_lengths.["82"]
        | FirstNChars 2 "83" as line -> CMEformat_lengths.["83"]
        | FirstNChars 2 "84" as line -> CMEformat_lengths.["84"]
        | FirstNChars 2 "91" as line -> CMEformat_lengths.["91"]
        | FirstNChars 2 "92" as line -> CMEformat_lengths.["92"]
        | _ as line -> 
            let id = line.Substring (0,1)
            CMEformat_lengths.[id]

    type ExchangeComplexHeader =
        {
            RecordId : string;
            ExchangeComplex_ClearingOrg_Acronym : string;
            BusinessDate : int64 option;
            SettlementOrIntradayFlag : string;
            FileIdentifier : string;
            BusinessTime : int option;
            FileCreationDate : int64 option;
            FileCreationTime : int option;
            FileFormat : string;
            GrossOrNetMargining : string; // not used according to spec
            LimitOptionValue : string; // not used according to spec
            BusinessFunction : string; // not used according to spec
            Filler1 : string; // not used according to spec
            ClearingHouseOrClientCode : string;
            Filler2 : string; // not used according to spec
            ClearingHouseOrClientAcronym : string;
            Filler3 : string;
            AccountTypeCode : string;
            Filler4 : string;
            AccountTypeAcronym : string;
            Filler5 : string;
            PerformanceBondClassCode : string;
            Filler6 : string;
            PerformanceBondClassAcronym : string;
            Filler7 : string;
            MaintenanceOrInitialCode : string;
            Filler8 : string;
            MaintenanceOrInitalAcronym : string;
            Filler9 : string;
        }

    let convertToExchangeComplexHeader (fields:string list) =
        {
            RecordId = fields.[0];
            ExchangeComplex_ClearingOrg_Acronym = fields.[1];
            BusinessDate = fields.[2] |> toInt64;
            SettlementOrIntradayFlag = fields.[3];
            FileIdentifier = fields.[4];
            BusinessTime = fields.[5] |> toInt;
            FileCreationDate = fields.[6] |> toInt64;
            FileCreationTime = fields.[7] |> toInt;
            FileFormat = fields.[8];
            GrossOrNetMargining = fields.[9];
            LimitOptionValue = fields.[10];
            BusinessFunction = fields.[11];
            Filler1 = fields.[12];
            ClearingHouseOrClientCode = fields.[13];
            Filler2 = fields.[14];
            ClearingHouseOrClientAcronym = fields.[15];
            Filler3 = fields.[16];
            AccountTypeCode = fields.[17];
            Filler4 = fields.[18];
            AccountTypeAcronym = fields.[19];
            Filler5 = fields.[20];
            PerformanceBondClassCode = fields.[21];
            Filler6 = fields.[22];
            PerformanceBondClassAcronym = fields.[23];
            Filler7 = fields.[24];
            MaintenanceOrInitialCode = fields.[25];
            Filler8 = fields.[26];
            MaintenanceOrInitalAcronym = fields.[27];
            Filler9 = fields.[28];
        }

    type ExchangeHeader = 
        {
            RecordId : string;
            ExchangeAcronym : string;
            Filler : string; // not used
            ExchangeCode : string;
            Filler2 : string;
        }

    let convertToExchangeHeader (fields:string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            Filler = fields.[2];
            ExchangeCode = fields.[3];
            Filler2 = fields.[4];
        }

    type Commodity' = 
        {
            CommodityCode : string;
            ContractType : string;
            RiskArrayValueDecimalLocator : int; // if blank or None then zero is assumed
            RiskArrayValueDecimalSign : string; // '+', '-' as expected or anything else means '+'
            Filler : string;
        }

    let private convertToCommodity (fields:string list) = 
        {
            CommodityCode = fields.[0];
            ContractType = fields.[1];
            RiskArrayValueDecimalLocator = match fields.[2] |> toInt with
                                            | Some(x) -> x
                                            | None -> 0;
            RiskArrayValueDecimalSign = match fields.[3] with
                                        | "+"
                                        | "-" as sgn -> sgn
                                        | _ -> "+";
            Filler = fields.[4];
        }

    type CombinedCommodity = 
        {
            RecordId : string;
            ExchangeAcronym : string;
            Filler : string; // not used
            CombinedCommodityCode : string;
            RiskExponent : int option;
            MarginCurrencyISOCode : string;
            MarginCurrencyCode : string;
            OptionMarginStyle : string;
            LimitOptionValue : string;
            CombinationMargin : string;
            Filler2 : string;
            Commodities : Commodity' list;
        }

    let convertToCombinedCommodity (fields:string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            Filler = fields.[2];
            CombinedCommodityCode = fields.[3];
            RiskExponent = fields.[4] |> toInt;
            MarginCurrencyISOCode = fields.[5];
            MarginCurrencyCode = fields.[6];
            OptionMarginStyle = fields.[7];
            LimitOptionValue = fields.[8];
            CombinationMargin = fields.[9];
            Filler2 = fields.[10];
            Commodities = Seq.skip 11 fields |> List.ofSeq |> groupInto 5 |> List.map convertToCommodity;
        }

    type Tier' =
        {
            TierNumber : int option;
            StartingContractMonth : int option;
            EndingContractMonth : int option;
        }

    let private convertToTier (fields:string list) =
        {
            TierNumber = fields.[0] |> toInt;
            StartingContractMonth = fields.[1] |> toInt;
            EndingContractMonth = fields.[2] |> toInt;
        }

    type TierDayCodes' =
        {
            StartingContractDayOrWeekCode : string;
            EndingContractDayOrWeekCode : string;
        }

    let private convertToTierDayCodes (fields : string list) =
        {
            StartingContractDayOrWeekCode = fields.[0];
            EndingContractDayOrWeekCode = fields.[1];
        }

    type IntraCommoditySpreadCharge = 
        {
            RecordId : string;
            CombinedCommodityCode : string;
            IntraCommoditySpreadChargeMethodCode : string;
            Tiers : Tier' list;
            Filler : string;
            InitialToMaintenanceRatio_Member : int option;
            InitialToMaintenanceRatio_Hedger : int option;
            InitialToMaintenanceRatio_Speculator : int option;
            TierDayCodes : TierDayCodes' list;
            Filler2 : string;
        }

    let convertToIntraCommoditySpreadCharge (fields:string list) =
        {
            RecordId = fields.[0];
            CombinedCommodityCode = fields.[1];
            IntraCommoditySpreadChargeMethodCode = fields.[2];
            Tiers = Seq.skip 3 fields |> Seq.take 12 |> List.ofSeq |> groupInto 3 |> List.map convertToTier;
            Filler = fields.[15];
            InitialToMaintenanceRatio_Member = fields.[16] |> toInt;
            InitialToMaintenanceRatio_Hedger = fields.[17] |> toInt;
            InitialToMaintenanceRatio_Speculator = fields.[18] |> toInt;
            TierDayCodes = fields |> Seq.skip 19 |> Seq.take 8 |> List.ofSeq |> groupInto 2 |> List.map convertToTierDayCodes;
            Filler2 = fields.[27];
        }

    type DeliveryMonth' =
        {
            MonthNumber : int option;
            ContractMonth : int option;
            ChargeRatePerDeltaConsumed : int option; //consumed by spreads
            ChargeRatePerDeltaRemaining : int option; //remaining in outrights
        }

    let private convertToDeliveryMonth (fields:string list) =
        {
            MonthNumber = fields.[0] |> toInt;
            ContractMonth = fields.[1] |> toInt;
            ChargeRatePerDeltaConsumed = fields.[2] |> toInt;
            ChargeRatePerDeltaRemaining = fields.[3] |> toInt;
        }

    type TableRisk' =
        {
            NumberOfMonths : int option;
            DeliveryMonth1 : DeliveryMonth';
            DeliveryMonth2 : DeliveryMonth';
            Filler : string;
        }

    let private convertToTableRisk (fields : string list) =
        {
            NumberOfMonths = fields.[0] |> toInt;
            DeliveryMonth1 = Seq.skip 1 fields |> Seq.take 4 |> List.ofSeq |> convertToDeliveryMonth;
            DeliveryMonth2 = Seq.skip 5 fields |> Seq.take 4 |> List.ofSeq |> convertToDeliveryMonth;
            Filler = fields.[9];
        }

    type BasisRisk' = 
        {
            SpotCommodityCode : string;
            BasisRiskChargeRate : int option;
            Filler : string;
        }

    let private convertToBasisRisk (fields : string list) =
        {
            SpotCommodityCode = fields.[0];
            BasisRiskChargeRate = fields.[1] |> toInt;
            Filler = fields.[2];
        }

    type SpotCalcMethod =
        | TableDriven of TableRisk'
        | BasisRisk of BasisRisk'
        | NoSpotCharge

    let private convertToSpotCalcMethod deliveryChargeMethodCode (field : string) =
        let lengths = ["10", [2;2;6;7;7;2;6;7;7;6;]; "11", [10;7;35;]] |> toDictionary first second
        let fields = 
            if deliveryChargeMethodCode = "10" ||  deliveryChargeMethodCode = "11" 
            then Seq.unfold splitter (field, lengths.[deliveryChargeMethodCode]) |> List.ofSeq
            else []
        match deliveryChargeMethodCode with
        | "10" -> TableDriven  (convertToTableRisk fields)
        | "11" -> BasisRisk  (convertToBasisRisk fields)
        | _ -> NoSpotCharge

    type SpotCharge =
        {
            RecordId : string;
            CombinedCommodityCode : string;
            Delivery_Spot_ChargeMethodCode : string;
            SpotCalcMethod : SpotCalcMethod;
            ShortOptionMinimumChargeRate : int option;
            RiskMaintenanceMarginAdjFactor_Members : int option;
            RiskMaintenanceMarginAdjFactor_Hedgers : int option;
            RiskMaintenanceMarginAdjFactor_Speculators : int option;
            ShortOptionMinimumCalcMethod : string;
            Filler2 : string;
        }

    let convertToSpotCharge (fields:string list) =
        {
            RecordId = fields.[0];
            CombinedCommodityCode = fields.[1];
            Delivery_Spot_ChargeMethodCode = fields.[2];
            SpotCalcMethod = convertToSpotCalcMethod (fields.[2]) fields.[3];
            ShortOptionMinimumChargeRate = fields.[4] |> toInt;
            RiskMaintenanceMarginAdjFactor_Members = fields.[5] |> toInt;
            RiskMaintenanceMarginAdjFactor_Hedgers = fields.[6] |> toInt;
            RiskMaintenanceMarginAdjFactor_Speculators = fields.[7] |> toInt;
            ShortOptionMinimumCalcMethod = fields.[8];
            Filler2 = fields.[9];
        }

    type CombinedCommodityGroup =
        {
            RecordId : string;
            CombinedCommodityGroupCode : string;
            Filler : string;
            CombinedCommodityCodes : string list;
            Filler2 : string;
        }

    let convertToCombinedCommodityGroup (fields:string list) =
        {
            RecordId = fields.[0];
            CombinedCommodityGroupCode = fields.[1];
            Filler = fields.[2];
            CombinedCommodityCodes = Seq.skip 3 fields |> Seq.take 10 |> List.ofSeq;
            Filler2 = fields.[13];
        }

    type ICSLeg' =
        {
            ExchangeAcronym : string;
            RequiredForScanningBasedSpread : string;
            CombinedCommodityCode : string;
            DeltaSpreadRatio : int option;
            SpreadSide : string;
        }

    let private convertToICSLeg (fields : string list) =
        {
            ExchangeAcronym = fields.[0];
            RequiredForScanningBasedSpread = fields.[1];
            CombinedCommodityCode = fields.[2];
            DeltaSpreadRatio = fields.[3] |> toInt;
            SpreadSide = fields.[4];
        }

    type InterCommoditySpread =
        {
            RecordId : string;
            CommodityGroupCode : string;
            SpreadPriority : int option;
            SpreadCreditRate : int option;
            Legs : ICSLeg' list;
            InterCommmoditySpreadMethodCode : string;
            M4TargetExchangeAcronym : string;
            M4TargetLegRequiredFlag : string;
            M4TargetCombinedCommodityCode : string;
            CreditCalcMethod : string;
            TierNumbers : string list;
            SpreadGroupFlag : string;
            M4TargetLegDeltaPerSpreadRatio : int option;
            M4MinLegsForSpread : int option;
            SpreadCreditRateDefinedSeparately : string;
            SpreadCreditRates : string list;
            RegulatoryStatusEligibilityCode : string;
        }

    let convertToInterCommoditySpread (fields : string list) =
        {
            RecordId = fields.[0];
            CommodityGroupCode = fields.[1];
            SpreadPriority = fields.[2] |> toInt;
            SpreadCreditRate = fields.[3] |> toInt;
            Legs = fields |> Seq.skip 4 |> Seq.take 20 |> List.ofSeq |> groupInto 5 |> List.map convertToICSLeg;
            InterCommmoditySpreadMethodCode = fields.[24];
            M4TargetExchangeAcronym = fields.[25];
            M4TargetLegRequiredFlag = fields.[26];
            M4TargetCombinedCommodityCode = fields.[27];
            CreditCalcMethod = fields.[28];
            TierNumbers = fields |> Seq.skip 29 |> Seq.take 4 |> List.ofSeq;
            SpreadGroupFlag = fields.[33];
            M4TargetLegDeltaPerSpreadRatio = fields.[34] |> toInt;
            M4MinLegsForSpread = fields.[35] |> toInt;
            SpreadCreditRateDefinedSeparately = fields.[36];
            SpreadCreditRates = fields |> Seq.skip 37 |> Seq.take 4 |> List.ofSeq;
            RegulatoryStatusEligibilityCode = fields.[35];
        }

    type Risk' =
        {
            Value : int option;
            Sign : string;
        }

    let private convertToRisk (fields:string list) =
        {
            Value = fields.[0] |> toInt;
            Sign = fields.[1];
        }

    type RiskArray81 =
        {
            RecordId : string;
            ExchangeAcronym : string;
            CommodityCode : string;
            UnderlyingCommodityCode : string;
            ProductTypeCode : string;
            OptionRightCode : string;
            FuturesContractMonth : int option;
            FuturesContractDayOrWeekCode : string;
            Filler : string;
            OptionContractMonth : int option;
            OptionContractDayOrWeekCode : string;
            Filler2 : string;
            OptionStrikePrice : int option;
            RiskArray : Risk' list;
            HighPrecisionSettlementPrice : int option;
            HighPrecisionSettlementPriceFlag : string;
        }

    let convertToRiskArray81 (fields:string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            CommodityCode = fields.[2];
            UnderlyingCommodityCode = fields.[3];
            ProductTypeCode = fields.[4];
            OptionRightCode = fields.[5];
            FuturesContractMonth = fields.[6] |> toInt;
            FuturesContractDayOrWeekCode = fields.[7];
            Filler = fields.[8];
            OptionContractMonth = fields.[9] |> toInt;
            OptionContractDayOrWeekCode = fields.[10];
            Filler2 = fields.[11];
            OptionStrikePrice = fields.[12] |> toInt;
            RiskArray = fields |> Seq.skip 13 |> Seq.take 18 |> List.ofSeq |> groupInto 2 |> List.map convertToRisk;
            HighPrecisionSettlementPrice = fields.[21] |> toInt;
            HighPrecisionSettlementPriceFlag = fields.[22];
        }

    type RiskArray82 =
        {
            RecordId : string;
            ExchangeAcronym : string;
            CommodityCode : string;
            UnderlyingCommodityCode : string;
            ProductTypeCode : string;
            OptionRightCode : string;
            FuturesContractMonth : int option;
            FuturesContractDayOrWeekCode : string;
            Filler : string;
            OptionContractMonth : int option;
            OptionContractDayOrWeekCode : string;
            Filler2 : string;
            OptionStrikePrice : int option;
            RiskArray : Risk' list;
            CompositeDelta : int option;
            SignForCompositeDelta : string;
            ImpliedVolatility : int option;
            SettlementPrice : int option;
            SignForSettlementPrice : string;
            SignForStrikePrice : string;
            CurrentDelta : int option;
            SignForCurrentDelta : string;
            CurrentDeltaBusinessDayFlag : string;
            StartOfDayPrice : int option;
            SignForStartOfDayPrice : string;
            ImpliedVolatilityExponent : int option;
            SignForImpliedVolatilityExponent : string;
            ContractValueFactor : int option;
            ContractValueFactorExponent : int option;
            SignForContractValueFactor : string;
            StrikeValueFactor : int option;
            StrikeValueFactorExponent : int option;
            SignForStrikeValueFactor : string;
        }

    let convertToRiskArray82 (fields:string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            CommodityCode = fields.[2];
            UnderlyingCommodityCode = fields.[3];
            ProductTypeCode = fields.[4];
            OptionRightCode = fields.[5];
            FuturesContractMonth = fields.[6] |> toInt;
            FuturesContractDayOrWeekCode = fields.[7];
            Filler = fields.[8];
            OptionContractMonth = fields.[9] |> toInt;
            OptionContractDayOrWeekCode = fields.[10];
            Filler2 = fields.[11];
            OptionStrikePrice = fields.[12] |> toInt;
            RiskArray = fields |> Seq.skip 13 |> Seq.take 14 |> List.ofSeq |> groupInto 2 |> List.map convertToRisk;
            CompositeDelta = fields.[27] |> toInt;
            SignForCompositeDelta = fields.[28];
            ImpliedVolatility = fields.[29] |> toInt;
            SettlementPrice = fields.[30] |> toInt;
            SignForSettlementPrice = fields.[31];
            SignForStrikePrice = fields.[32];
            CurrentDelta = fields.[33] |> toInt;
            SignForCurrentDelta = fields.[34];
            CurrentDeltaBusinessDayFlag = fields.[35];
            StartOfDayPrice = fields.[36] |> toInt;
            SignForStartOfDayPrice = fields.[37];
            ImpliedVolatilityExponent = fields.[38] |> toInt;
            SignForImpliedVolatilityExponent = fields.[39];
            ContractValueFactor = fields.[40] |> toInt;
            ContractValueFactorExponent = fields.[41] |> toInt;
            SignForContractValueFactor = fields.[42];
            StrikeValueFactor = fields.[43] |> toInt;
            StrikeValueFactorExponent = fields.[44] |> toInt;
            SignForStrikeValueFactor = fields.[45];
        }

    type Risk2' =
        {
            Value : string;
            Sign : string;
        }

    let private convertToRisk2 (fields : string list) =
        {
            Value = fields.[0];
            Sign = fields.[1];
        }

    type RiskArray83 = 
        {
            RecordId : string;
            ExchangeAcronym : string;
            CommodityCode : string;
            UnderlyingCommodityCode : string;
            ProductTypeCode : string;
            OptionRightCode : string;
            FuturesContractMonth : int64 option;
            FuturesContractDayOrWeekCode : string;
            Filler : string;
            OptionContractMonth : int64 option;
            OptionContractDayOrWeekCode : string;
            Filler2 : string;
            OptionStrikePrice : int option;
            RiskArray : Risk2' list;
        }

    let convertToRiskArray83 (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            CommodityCode = fields.[2];
            UnderlyingCommodityCode = fields.[3];
            ProductTypeCode = fields.[4];
            OptionRightCode = fields.[5];
            FuturesContractMonth = fields.[6] |> toInt64;
            FuturesContractDayOrWeekCode = fields.[7];
            Filler = fields.[8];
            OptionContractMonth = fields.[9] |> toInt64;
            OptionContractDayOrWeekCode = fields.[10];
            Filler2 = fields.[11];
            OptionStrikePrice = fields.[12] |> toInt;
            RiskArray = fields |> Seq.skip 13 |> List.ofSeq |> groupInto 2 |> List.map convertToRisk2;
        }

    type RiskArray84 = 
        {
            RecordId : string;
            ExchangeAcronym : string;
            CommodityCode : string;
            UnderlyingCommodityCode : string;
            ProductTypeCode : string;
            OptionRightCode : string;
            FuturesContractMonth : int64 option;
            FuturesContractDayOrWeekCode : string;
            Filler : string;
            OptionContractMonth : int64 option;
            OptionContractDayOrWeekCode : string;
            Filler2 : string;
            OptionStrikePrice : int option;
            RiskArray : Risk2' list;
            CompositeDelta : int option;
            SignForCompositeDelta : string;
            ImpliedVolatility : int option;
            SettlementPrice : int option;
            SignForSettlementPrice : string;
            SignForStrikePrice : string;
            CurrentDelta : int option;
            SignForCurrentDelta : string;
            CurrentDeltaBusinessDayFlag : string;
            StartOfDayPrice : int option;
            SignForStartOfDayPrice : string;
            ImpliedVolatilityExponent : int option;
            SignForImpliedVolatilityExponent : string;
            ContractValueFactor : int option;
            ContractValueFactorExponent : int option;
            SignForContractValueFactor : string;
            StrikeValueFactor : int option;
            StrikeValueFactorExponent : int option;
            SignForStrikeValueFactor : string;
        }

    let convertToRiskArray84 (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            CommodityCode = fields.[2];
            UnderlyingCommodityCode = fields.[3];
            ProductTypeCode = fields.[4];
            OptionRightCode = fields.[5];
            FuturesContractMonth = fields.[6] |> toInt64;
            FuturesContractDayOrWeekCode = fields.[7];
            Filler = fields.[8];
            OptionContractMonth = fields.[9] |> toInt64;
            OptionContractDayOrWeekCode = fields.[10];
            Filler2 = fields.[11];
            OptionStrikePrice = fields.[12] |> toInt;
            RiskArray = fields |> Seq.skip 13 |> Seq.take 14 |> List.ofSeq |> groupInto 2 |> List.map convertToRisk2;
            CompositeDelta = fields.[27] |> toInt;
            SignForCompositeDelta = fields.[28];
            ImpliedVolatility = fields.[29] |> toInt;
            SettlementPrice = fields.[30] |> toInt;
            SignForSettlementPrice = fields.[31];
            SignForStrikePrice = fields.[32];
            CurrentDelta = fields.[33] |> toInt;
            SignForCurrentDelta = fields.[34];
            CurrentDeltaBusinessDayFlag = fields.[35];
            StartOfDayPrice = fields.[36] |> toInt;
            SignForStartOfDayPrice = fields.[37];
            ImpliedVolatilityExponent = fields.[38] |> toInt;
            SignForImpliedVolatilityExponent = fields.[39];
            ContractValueFactor = fields.[40] |> toInt;
            ContractValueFactorExponent = fields.[41] |> toInt;
            SignForContractValueFactor = fields.[42];
            StrikeValueFactor = fields.[43] |> toInt;
            StrikeValueFactorExponent = fields.[44] |> toInt;
            SignForStrikeValueFactor = fields.[45];
        }

    type DebtSecurity91 =
        {
            RecordId : string;
            ExchangeAcronym : string;
            Filler : string;
            TargetCommodityCode : string;
            TargetContractMonth : int64 option;
            Filler2 : string;
            IssuingCountryCode : string;
            Filler3 : string;
            PrimaryInstId : string;
            DenominationCurrencyISO : string;
            DenominationCurrencyCode : string;
            MaturityDate : int64 option;
            CouponRate : int option;
            PhysicalConversionFactor : int option;
            Filler4 : string;
        }

    let convertToDebtSecurity91 (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            Filler = fields.[2];
            TargetCommodityCode = fields.[3];
            TargetContractMonth = fields.[4] |> toInt64;
            Filler2 = fields.[5];
            IssuingCountryCode = fields.[6];
            Filler3 = fields.[7];
            PrimaryInstId = fields.[8];
            DenominationCurrencyISO = fields.[9];
            DenominationCurrencyCode = fields.[10];
            MaturityDate = fields.[11] |> toInt64;
            CouponRate = fields.[12] |> toInt;
            PhysicalConversionFactor = fields.[13] |> toInt;
            Filler4 = fields.[14];
        }

    type DebtSecurity92 =
        {
            RecordId : string;
            ExchangeAcronym : string;
            Filler : string;
            TargetCommodityCode : string;
            TargetContractMonth : int64 option;
            Filler2 : string;
            IssuingCountryCode : string;
            Filler3 : string;
            PrimaryInstId : string;
            InstrumentDescription : string;
            LongBondEquivalenceFactor : int option;
            Filler4 : string;
        }

    let convertToDebtSecurity92 (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            Filler = fields.[2];
            TargetCommodityCode = fields.[3];
            TargetContractMonth = fields.[4] |> toInt64;
            Filler2 = fields.[5];
            IssuingCountryCode = fields.[6];
            Filler3 = fields.[7];
            PrimaryInstId = fields.[8];
            InstrumentDescription = fields.[9];
            LongBondEquivalenceFactor = fields.[10] |> toInt;
            Filler4 = fields.[11];
        }

    type ArrayCalculationParameters =
        {
            RecordId : string;
            ExchangeAcronym : string;
            CommodityCode : string;
            ProductTypeCode : string;
            FuturesContractMonth : int option;
            FuturesContractDayOrWeekCode : string;
            Filler : string;
            OptionContractMonth : int option;
            OptionContractDayOrWeekCode : string;
            Filler2 : string;
            BaseVolatility : int option;
            VolatilityScanRange : int option;
            FuturesPriceScanRange : int option;
            ExtremeMoveMultiplier : int option;
            ExtremeMoveCoveredFraction : int option;
            InterestRate : int option;
            TimeToExpiration : int option;
            LookaheadTime : int option;
            DeltaScalingFactor : int option;
            ExpirationDate : int64 option;
            UnderlyingCommodityCode : string;
            PricingModel : string;
            CouponOrDivYield : int option;
            OptionExpirationReferencePriceFlag : string;
            OptionExpirationReferencePrice : int option;
            OptionExpirationReferencePriceSign : string;
            SwapValueFactor : int option;
            SwapValueFactorExponent : int option;
            SignForSwapValueFactorExponent : string;
            BaseVolatilityExponent : int option;
            SignForBaseVolatilityExponent : string;
            VolatilityScanRangeExponent : int option;
            SignForVolatilityScanRangeExponent : string;
            DiscountFactor : int option;
            VolatilityScanRangeQuotationMethod : string;
            PriceScanRangeQuotationMethod : string;
            FuturesPriceScanRangeExponent : int option;
            SignForFuturesPriceScanRangeExponent : string;
        }

    let convertToArrayCalculationParameters (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            CommodityCode = fields.[2];
            ProductTypeCode = fields.[3];
            FuturesContractMonth = fields.[4] |> toInt;
            FuturesContractDayOrWeekCode = fields.[5];
            Filler = fields.[6];
            OptionContractMonth = fields.[7] |> toInt;
            OptionContractDayOrWeekCode = fields.[8];
            Filler2 = fields.[9];
            BaseVolatility = fields.[10] |> toInt;
            VolatilityScanRange = fields.[11] |> toInt;
            FuturesPriceScanRange = fields.[12] |> toInt;
            ExtremeMoveMultiplier = fields.[13] |> toInt;
            ExtremeMoveCoveredFraction = fields.[14] |> toInt;
            InterestRate = fields.[15] |> toInt;
            TimeToExpiration = fields.[16] |> toInt;
            LookaheadTime = fields.[17] |> toInt;
            DeltaScalingFactor = fields.[18] |> toInt;
            ExpirationDate = fields.[19] |> toInt64;
            UnderlyingCommodityCode = fields.[20];
            PricingModel = fields.[21];
            CouponOrDivYield = fields.[22] |> toInt;
            OptionExpirationReferencePriceFlag = fields.[21];
            OptionExpirationReferencePrice = fields.[22] |> toInt;
            OptionExpirationReferencePriceSign = fields.[23];
            SwapValueFactor = fields.[24] |> toInt;
            SwapValueFactorExponent = fields.[25] |> toInt;
            SignForSwapValueFactorExponent = fields.[26];
            BaseVolatilityExponent = fields.[27] |> toInt;
            SignForBaseVolatilityExponent = fields.[28];
            VolatilityScanRangeExponent = fields.[29] |> toInt;
            SignForVolatilityScanRangeExponent = fields.[30];
            DiscountFactor = fields.[31] |> toInt;
            VolatilityScanRangeQuotationMethod = fields.[32];
            PriceScanRangeQuotationMethod = fields.[33];
            FuturesPriceScanRangeExponent = fields.[34] |> toInt;
            SignForFuturesPriceScanRangeExponent = fields.[35];
        }

    type Leg' = 
        {
            LegNumber : int option;
            TierNumber : int option;
            DeltaPerSpreadRatio : int option;
            MarketSide : string;
        }

    let private convertToLeg (fields : string list) =
        {
            LegNumber = fields.[0] |> toInt;
            TierNumber = fields.[1] |> toInt;
            DeltaPerSpreadRatio = fields.[2] |> toInt;
            MarketSide = fields.[3];
        }

    type TierToTierIntraCommoditySpread =
        {
            RecordId : string;
            CombinedCommodityCode : string;
            IntraCommoditySpreadMethodCode : string;
            SpreadPriority : int option;
            NumberOfLegs : int option;
            ChargeRate : int option;
            Legs : Leg' list;
            Filler : string;
        }

    let convertToTierToTierIntraCommoditySpread (fields : string list) =
        {
            RecordId = fields.[0];
            CombinedCommodityCode = fields.[1];
            IntraCommoditySpreadMethodCode = fields.[2];
            SpreadPriority = fields.[3] |> toInt;
            NumberOfLegs = fields.[4] |> toInt;
            ChargeRate = fields.[5] |> toInt;
            Legs = fields |> Seq.skip 6 |> Seq.take 32 |> List.ofSeq |> groupInto 4 |> List.map convertToLeg;
            Filler = fields.[38];
        }

    type Leg'' =
        {
            ContractMonth : int option;
            RemainingPartOfContractMonth : string;
            DeltaPerSpreadRatio : int option;
            MarketSide : string;
        }

    let private convertToLeg'' (fields : string list) =
        {
            ContractMonth = fields.[0] |> toInt;
            RemainingPartOfContractMonth = fields.[1];
            DeltaPerSpreadRatio = fields.[2] |> toInt;
            MarketSide = fields.[3];
        }

    type SeriesToSeriesIntraCommoditySpread =
        {
            RecordId : string;
            CombinedCommodityCode : string;
            SpreadPriority : int option;
            ChargeRate : int option;
            Legs : Leg'' list;
            Filler : string;
        }

    let convertToSeriesToSeriesIntraCommoditySpread (fields : string list) =
        {
            RecordId = fields.[0];
            CombinedCommodityCode = fields.[1];
            SpreadPriority = fields.[2] |> toInt;
            ChargeRate = fields.[3] |> toInt;
            Legs = fields |> Seq.skip 4 |> Seq.take 16 |> List.ofSeq |> groupInto 4 |> List.map convertToLeg'';
            Filler = fields.[20];
        }

    type PriceConversionParameter =
        {
            RecordId : string;
            ExchangeAcronym : string;
            ProductCode : string;
            ProductTypeCode : string;
            ProductName : string;
            SettlementPriceDecimalLocator : int option;
            StrikePriceDecimalLocator : int option;
            SettlementPriceAlignmentCode : string;
            StrikePriceAlignmentCode : string;
            ContractValueFactor : int option;
            StandardCabinetOptionValue : int option;
            QuotedPositionQuantityPerContract : int option;
            SettlementCurrencyISO : string;
            SettlementCurrencyCode : string;
            PriceQuotationMethod : string;
            SignForContractValueFactor : string;
            ContractValueFactorExponent : int option;
            ExerciseStyle : string;
            ProductLongName : string;
            PositionableProductIndicator : string;
            MoneyCalculationMethod : string;
            ValuationMethod : string;
            SettlementMethod : string;
            FXSpotDateCollateralisaationGainCreditRate : int option;
            FXPreSpotDateCollateralisaationGainCreditRate : int option;
            FXForwardCollateralisaationGainCreditRate : int option;
            EquivalentPositionFactor : int option;
            EquivalentPositionFactorExponent : int option;
            SignForEquivalentPositionFactorExponent : string;
            VariableTickOptionFlag : string;
        }

    let convertToPriceConversionParameter (fields : string list ) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            ProductCode = fields.[2];
            ProductTypeCode = fields.[3];
            ProductName = fields.[4];
            SettlementPriceDecimalLocator = fields.[5] |> toInt;
            StrikePriceDecimalLocator = fields.[6] |> toInt;
            SettlementPriceAlignmentCode = fields.[7];
            StrikePriceAlignmentCode = fields.[8];
            ContractValueFactor = fields.[9] |> toInt;
            StandardCabinetOptionValue = fields.[10] |> toInt;
            QuotedPositionQuantityPerContract = fields.[11] |> toInt;
            SettlementCurrencyISO = fields.[12];
            SettlementCurrencyCode = fields.[13];
            PriceQuotationMethod = fields.[14];
            SignForContractValueFactor = fields.[15];
            ContractValueFactorExponent = fields.[16] |> toInt;
            ExerciseStyle = fields.[17];
            ProductLongName = fields.[18];
            PositionableProductIndicator = fields.[19];
            MoneyCalculationMethod = fields.[20];
            ValuationMethod = fields.[21];
            SettlementMethod = fields.[22];
            FXSpotDateCollateralisaationGainCreditRate = fields.[23] |> toInt;
            FXPreSpotDateCollateralisaationGainCreditRate = fields.[24] |> toInt;
            FXForwardCollateralisaationGainCreditRate = fields.[25] |> toInt;
            EquivalentPositionFactor = fields.[26] |> toInt;
            EquivalentPositionFactorExponent = fields.[27] |> toInt;
            SignForEquivalentPositionFactorExponent = fields.[28];
            VariableTickOptionFlag = fields.[29];
        }

    type CommodityRedefinition =
        {
            RecordId : string;
            ExchangeAcronym : string;
            ProductCode : string;
            CombinedCommodityCode : string;
            AlternateExchangeAcronym : string;
            AlternateProductCode : string;
            AlternateCombinedCommodityCode : string;
            Filler : string;
        }

    let convertToCommodityRedefinition (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            ProductCode = fields.[2];
            CombinedCommodityCode = fields.[3];
            AlternateExchangeAcronym = fields.[4];
            AlternateProductCode = fields.[5];
            AlternateCombinedCommodityCode = fields.[6];
            Filler = fields.[7];
        }

    type TierDayCode' =
        {
            StartingContractDayOrWeekCode : string;
            EndingContractDayOrWeekCode : string;
        }

    let private convertToTierDayCode (fields : string list) =
        {
            StartingContractDayOrWeekCode = fields.[0];
            EndingContractDayOrWeekCode = fields.[1];
        }

    type ScanningTier = 
        {
            RecordId : string;
            CombinedCommodityCode : string;
            ScanningOrInterCommoditySpreadingMethodCode : string;
            NumberOfTiers : int option;
            Tiers : Tier' list;
            WeightedFuturesPriceRiskCalcMethod : string;
            TierDayCodes : TierDayCode' list;
            TierShortOptionMinChargeRates : int option list;
        }

    let convertToScanningTier (fields : string list) =
        {
            RecordId = fields.[0];
            CombinedCommodityCode = fields.[1];
            ScanningOrInterCommoditySpreadingMethodCode = fields.[2];
            NumberOfTiers = fields.[3] |> toInt;
            Tiers = fields |> Seq.skip 4 |> Seq.take 15 |> List.ofSeq |> groupInto 3 |> List.map convertToTier;
            WeightedFuturesPriceRiskCalcMethod = fields.[19];
            TierDayCodes = fields |> Seq.skip 20 |> Seq.take 10 |> List.ofSeq |> groupInto 2 |> List.map convertToTierDayCode;
            TierShortOptionMinChargeRates = fields |> Seq.skip 30 |> Seq.map toInt |> List.ofSeq;
        }

    type CurrencyConversionRate =
        {
            RecordId : string;
            FromCurrencyISOCode : string;
            FromCurrencyByteCode : string;
            ToCurrencyISOCode : string;
            ToCurrencyByteCode : string;
            ConversionMultiplier : int option;
        }

    let convertToCurrencyConversionRate (fields:string list) =
        {
            RecordId = fields.[0];
            FromCurrencyISOCode = fields.[1];
            FromCurrencyByteCode = fields.[2];
            ToCurrencyISOCode = fields.[3];
            ToCurrencyByteCode = fields.[4];
            ConversionMultiplier = fields.[5] |> toInt;
        }

    type DailyAdjustmentRate =
        {
            RecordId : string;
            ExchangeAcronym : string;
            ProductCode : string;
            FuturesContractMonth : string;
            FuturesContractDayOrWeekCode : string;
            BusinessDate : string;
            DailyAdjRateLong : int option;
            DailyAdjSign : string;
            DailyAdjPremiumDiscount : string;
            DailyAdjRateShort : int option;
            DailyShortRateSign : string;
            DailyShortPremiumDiscount : string;
            ShortRateFlag : string;
            LongPositionValueMaintenanceRate : string;
            ShortPositionValueMaintenanceRate : string;
            ResetLongMarginPriceFlag : string;
            ResetLongDownThreshold : string;
            ResetLongUpThreshold : string;
            ResetShortMarginPriceFlag : string;
            ResetShortDownThreshold : string;
            ResetShortUpThreshold : string;
            ValueMaintenanceProductClass : string;
            Filler : string;
        }

    let convertToDailyAdjustmentRate (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            ProductCode = fields.[2];
            FuturesContractMonth = fields.[3];
            FuturesContractDayOrWeekCode = fields.[4];
            BusinessDate = fields.[5];
            DailyAdjRateLong = fields.[6] |> toInt;
            DailyAdjSign = fields.[7];
            DailyAdjPremiumDiscount = fields.[8];
            DailyAdjRateShort = fields.[9] |> toInt;
            DailyShortRateSign = fields.[10];
            DailyShortPremiumDiscount = fields.[11];
            ShortRateFlag = fields.[12];
            LongPositionValueMaintenanceRate = fields.[13];
            ShortPositionValueMaintenanceRate = fields.[14];
            ResetLongMarginPriceFlag = fields.[15];
            ResetLongDownThreshold = fields.[16];
            ResetLongUpThreshold = fields.[17];
            ResetShortMarginPriceFlag = fields.[18];
            ResetShortDownThreshold = fields.[19];
            ResetShortUpThreshold = fields.[20];
            ValueMaintenanceProductClass = fields.[21];
            Filler = fields.[22];
        }

    type CombinationMarginingMethod =
        {
            RecordId : string;
            CombinationMarginingMethodCode : string;
            ProductCode : string;
            PriceOffset : int option;
        }

    let convertToCombinationMarginingMethod (fields : string list) =
        {
            RecordId = fields.[0];
            CombinationMarginingMethodCode = fields.[1];
            ProductCode = fields.[2];
            PriceOffset = fields.[3] |> toInt;
        }

    type OptionOnCombinationProductFamilyDefinition =
        {
            RecordId : string;
            CombinationProductCode : string;
            OptionOnCombinationProductCode : string;
        }

    let convertToOptionOnCombinationProductFamilyDefinition (fields : string list) =
        {
            RecordId = fields.[0];
            CombinationProductCode = fields.[1];
            OptionOnCombinationProductCode = fields.[2];
        }

    type CombinationUnderlyingLegs =
        {
            RecordId : string;
            ExchangeAcronym : string;
            CombinationProductCode : string;
            CombinationType : string;
            CombinationContractMonth : string;
            CombinationContractDay : string;
            Filler : string;
            LegNumber : int option;
            LegRelationship : string;
            LegRatio : int option;
            LegProductCode : string;
            LegProductType: string;
            LegContractMonth : string;
            LegContractDay : string;
            LegRatioFraction : string;
            LegPriceAvailableFlag : string;
            LegPriceUsageFlag : string;
            LegPrice : int option;
            LegPriceSign : string;
        }

    let convertToCombinationUnderlyingLegs (fields : string list) =
        {
            RecordId = fields.[0];
            ExchangeAcronym = fields.[1];
            CombinationProductCode = fields.[2];
            CombinationType = fields.[3];
            CombinationContractMonth = fields.[4];
            CombinationContractDay = fields.[5];
            Filler = fields.[6];
            LegNumber = fields.[7] |> toInt;
            LegRelationship = fields.[8];
            LegRatio = fields.[9] |> toInt;
            LegProductCode = fields.[10];
            LegProductType = fields.[11];
            LegContractMonth = fields.[12];
            LegContractDay = fields.[13];
            LegRatioFraction = fields.[14];
            LegPriceAvailableFlag = fields.[15];
            LegPriceUsageFlag = fields.[16];
            LegPrice = fields.[17] |> toInt;
            LegPriceSign = fields.[18];
        }

    type LineType =
        | ExchangeComplexHeader of ExchangeComplexHeader
        | ExchangeHeader of ExchangeHeader
        | CombinedCommodity of CombinedCommodity
        | IntraCommoditySpreadCharge of IntraCommoditySpreadCharge
        | SpotCharge of SpotCharge
        | CombinedCommodityGroup of CombinedCommodityGroup
        | InterCommoditySpread of InterCommoditySpread
        | RiskArray81 of RiskArray81
        | RiskArray82 of RiskArray82
        | RiskArray83 of RiskArray83
        | RiskArray84 of RiskArray84
        | DebtSecurity91 of DebtSecurity91
        | DebtSecurity92 of DebtSecurity92
        | ArrayCalculationParameters of ArrayCalculationParameters
        | TierToTierIntraCommoditySpread of TierToTierIntraCommoditySpread
        | SeriesToSeriesIntraCommoditySpread of SeriesToSeriesIntraCommoditySpread
        | PriceConversionParameter of PriceConversionParameter
        | CommodityRedefinition of CommodityRedefinition
        | ScanningTier of ScanningTier
        | CurrencyConversionRate of CurrencyConversionRate
        | DailyAdjustmentRate of DailyAdjustmentRate
        | CombinationMarginingMethod of CombinationMarginingMethod
        | OptionOnCombinationProductFamilyDefinition of OptionOnCombinationProductFamilyDefinition
        | CombinationUnderlyingLegs of CombinationUnderlyingLegs
        | Unknown of string

    let convert (fields:string list) =
        match fields.[0].Trim() with
        | "0" -> ExchangeComplexHeader (convertToExchangeComplexHeader fields)
        | "1" -> ExchangeHeader (convertToExchangeHeader fields)
        | "2" -> CombinedCommodity (convertToCombinedCommodity fields)
        | "3" -> IntraCommoditySpreadCharge (convertToIntraCommoditySpreadCharge fields)
        | "4" -> SpotCharge (convertToSpotCharge fields)
        | "5" -> CombinedCommodityGroup (convertToCombinedCommodityGroup fields)
        | "6" -> InterCommoditySpread (convertToInterCommoditySpread fields)
        | "81" -> RiskArray81  (convertToRiskArray81 fields)
        | "82" -> RiskArray82 (convertToRiskArray82 fields)
        | "83" -> RiskArray83  (convertToRiskArray83 fields)
        | "84" -> RiskArray84 (convertToRiskArray84 fields)
        | "91" -> DebtSecurity91 (convertToDebtSecurity91 fields)
        | "92" -> DebtSecurity92 (convertToDebtSecurity92 fields)
        | "B" -> ArrayCalculationParameters (convertToArrayCalculationParameters fields)
        | "C" -> TierToTierIntraCommoditySpread (convertToTierToTierIntraCommoditySpread fields)
        | "E" -> SeriesToSeriesIntraCommoditySpread (convertToSeriesToSeriesIntraCommoditySpread fields)
        | "P" -> PriceConversionParameter (convertToPriceConversionParameter fields)
        | "R" -> CommodityRedefinition (convertToCommodityRedefinition fields)
        | "S" -> ScanningTier (convertToScanningTier fields)
        | "T" -> CurrencyConversionRate (convertToCurrencyConversionRate fields)
        | "V" -> DailyAdjustmentRate (convertToDailyAdjustmentRate fields)
        | "X" -> CombinationMarginingMethod (convertToCombinationMarginingMethod fields)
        | "Y" -> OptionOnCombinationProductFamilyDefinition (convertToOptionOnCombinationProductFamilyDefinition fields)
        | "Z" -> CombinationUnderlyingLegs (convertToCombinationUnderlyingLegs fields)
        | _ as str -> Unknown (str)

    type tree =
        | Node of LineType * tree list

    let buildTree records =
        let buildL4 recs =
            let rec build l acc =
                match l with 
                | ArrayCalculationParameters(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | TierToTierIntraCommoditySpread(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | SeriesToSeriesIntraCommoditySpread(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | ScanningTier(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | DailyAdjustmentRate(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | CombinationMarginingMethod(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | OptionOnCombinationProductFamilyDefinition(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | CombinationUnderlyingLegs(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | _ -> l, acc
            build recs []

        let buildL3 recs =
            let rec build l acc =
                match l with 
                | CombinedCommodity(_) as record :: tl -> 
                    let remainder, l4 = buildL4 tl
                    build remainder (Node (record, l4) :: acc)
                | IntraCommoditySpreadCharge(_) as record :: tl -> 
                    let remainder, l4 = buildL4 tl
                    build remainder (Node (record, l4) :: acc)
                | SpotCharge(_) as record :: tl -> 
                    let remainder, l4 = buildL4 tl
                    build remainder (Node (record, l4) :: acc)
                | InterCommoditySpread(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | CombinedCommodityGroup(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | RiskArray81(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | RiskArray82(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | RiskArray83(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | RiskArray84(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | DebtSecurity91(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | DebtSecurity92(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | PriceConversionParameter(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | CommodityRedefinition(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | _ -> l, acc
            build recs []

        let buildL2 recs =
            let rec build l acc =
                match l with 
                | ExchangeHeader(_) as record :: tl -> 
                    let remainder, l3 = buildL3 tl
                    build remainder (Node (record, l3) :: acc)
                | CurrencyConversionRate(_) as record :: tl -> build tl (Node (record, []) :: acc)
                | _ -> l, acc
            build recs []

        match records with
        | ExchangeComplexHeader(_) as record :: tl ->
            let remainder, l2 = buildL2 tl
            remainder, Node (record, l2)
