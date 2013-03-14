module Shaftesbury.Span.HK.ExpandedFormat
open Shaftesbury.FSharp.Utils

let HKformat_lengths =
    [
        "0", [2;6;8;1;2;4;8;4;2;1;1;5;6;1;1;5;];
        "1", [2;3;2;2;];
        "2", [2;3;1;6;1;3;1;1;1;1;2;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;10;3;1;1;1;15;];
        "3", [2;6;2;2;6;6;2;6;6;2;6;6;2;6;6;];
        "4", [2;6;2;2;2;6;7;7;2;6;7;7;6;7;3;3;3;1;53;];
        "5", [2;3;7;6;6;6;6;6;6;6;6;6;6;];
        "81", [2;3;10;10;3;1;6;2;1;6;2;1;7;5;1;5;1;5;1;5;1;5;1;5;1;5;1;5;1;5;1;];
        "82", [2;3;10;10;3;1;6;2;1;6;2;1;7;5;1;5;1;5;1;5;1;5;1;5;1;5;1;5;1;8;7;1;];
        "B", [2;3;10;3;6;2;1;6;2;1;8;8;5;5;5;5;7;6;6;8;10;2;8;];
        "C", [2;6;2;2;2;7;2;2;2;1;2;2;2;1;];
        "T", [2;3;1;3;1;10;];
    ] |> toDictionary first second

let findLengthArray = function
    | FirstNChars 2 "81" as line -> HKformat_lengths.["81"]
    | FirstNChars 2 "82" as line -> HKformat_lengths.["82"]
    | _ as line -> 
        let id = line.Substring (0,1)
        HKformat_lengths.[id]

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
    }

type ExchangeHeader = 
    {
        RecordId : string;
        ExchangeAcronym : string;
        Filler : string; // not used
        ExchangeCode : string;
    }

let convertToExchangeHeader (fields:string list) =
    {
        RecordId = fields.[0];
        ExchangeAcronym = fields.[1];
        Filler = fields.[2];
        ExchangeCode = fields.[3];
    }

type Commodity' = 
    {
        CommodityCode : string;
        ContractType : string;
        RiskArrayValueDecimalLocator : int; // if blank or None then zero is assumed
        RiskArrayValueDecimalSign : string; // '+', '-' as expected or anything else means '+'
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
        Commodities = Seq.skip 11 fields |> groupIn 4 |> Seq.map convertToCommodity |> List.ofSeq;
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

type IntraCommoditySpreadCharge = 
    {
        RecordId : string;
        CombinedCommodityCode : string;
        IntraCommoditySpreadChargeMethodCode : string;
        Tiers : Tier' list;
    }

let convertToIntraCommoditySpreadCharge (fields:string list) =
    {
        RecordId = fields.[0];
        CombinedCommodityCode = fields.[1];
        IntraCommoditySpreadChargeMethodCode = fields.[2];
        Tiers = Seq.skip 3 fields |> groupIn 3 |> Seq.map convertToTier |> List.ofSeq;
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

type SpotCharge =
    {
        RecordId : string;
        CombinedCommodityCode : string;
        Delivery_Spot_ChargeMethodCode : string;
        NumberOfMonths : int option;
        DeliveryMonth1 : DeliveryMonth';
        DeliveryMonth2 : DeliveryMonth';
        Filler : string;
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
        NumberOfMonths = fields.[3] |> toInt;
        DeliveryMonth1 = Seq.skip 4 fields |> Seq.take 4 |> List.ofSeq |> convertToDeliveryMonth;
        DeliveryMonth2 = Seq.skip 8 fields |> Seq.take 4 |> List.ofSeq |> convertToDeliveryMonth;
        Filler = fields.[12];
        ShortOptionMinimumChargeRate = fields.[13] |> toInt;
        RiskMaintenanceMarginAdjFactor_Members = fields.[14] |> toInt;
        RiskMaintenanceMarginAdjFactor_Hedgers = fields.[15] |> toInt;
        RiskMaintenanceMarginAdjFactor_Speculators = fields.[16] |> toInt;
        ShortOptionMinimumCalcMethod = fields.[17];
        Filler2 = fields.[18];
    }

type CombinedCommodityGroup =
    {
        RecordId : string;
        CombinedCommodityGroupCode : string;
        Filler : string;
        CombinedCommodityCodes : string list;
    }

let convertToCombinedCommodityGroup (fields:string list) =
    {
        RecordId = fields.[0];
        CombinedCommodityGroupCode = fields.[1];
        Filler = fields.[2];
        CombinedCommodityCodes = Seq.skip 3 fields |> List.ofSeq;
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
    }

let convertToRiskArray81 (fields:string list) =

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
    }
