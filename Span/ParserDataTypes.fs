module Shaftesbury.Span.ParserDataTypes
 open Shaftesbury.FSharp.Utils
 open Shaftesbury.Span.Span4RowTypes
 type ContractExpiryDetailsTree =
   {
     ContractExpiryDetails : ContractExpiryDetails;
     SeriesDetails : RiskArray list;
   }
 type ContractDetailsTree =
   {
     ContractDetails : ContractDetails;
     Expiries : ContractExpiryDetailsTree list;
   }
 type CombinedContractDetailsTree =
   {
     CombinedContractDetails : CombinedContract;
     MonthTierDetails : MonthTierDetails list;
     LegSpreadDetails : LegSpreadDetails list;
     PromptDateChargeDetails : PromptDateChargeDetails list;
     IntercontractTierDetails : IntercontractTierDetails list;
     StrategySpreadDetails : StrategySpreadDetails list;
     Contracts : ContractDetailsTree list;
   }
 type ExchangeDetailsTree =
   {
     ExchangeDetails : ExchangeDetails;
     CombinedContractDetails : CombinedContractDetailsTree list;
   }
 type CommonData =
   {
     Header : SPANHeader;
     ContractTypeMap : ContractTypeMapping list;
     Currency : Currency list;
     ExchangeRates : ExchangeRate list;
     Spreads : InterContractSpread list;
     Scenarios : Scenario list;
     MarginGroups : MarginGroup list;
   }
 type SpanTree =
   CommonData * ExchangeDetailsTree list
