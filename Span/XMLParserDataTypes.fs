module Shaftesbury.Span.XMLParserDataTypes
 // Level 9
 type SpanXMLDiv =
   | Val of float
   | Dtm of int
   | SetlDate of int
 // Level 8
 type SpanXMLRa =
   | R of int
   | A of float // list;
   | D of float
 type SpanXMLDivRate =
   | Val of float
   | Div of SpanXMLDiv list
 type SpanXMLUndC =
   | Exch of string
   | PfId of int
   | CId of int
   | S of string
   | I of float
 type SpanXMLIntrRate =
   | Val of float
   | Rl of int
   | Cpm of int
   | Exm of int
 type SpanXMLOpt =
   | CId of int
   | O of string
   | K of float
   | P of float
   | Pq of int
   | D of float
   | V of float
   | Val of float
   | Cvf of float
   | Svf of float
   | Ra of SpanXMLRa list
 type SpanXMLRate =
   | R of int
   | Val of float
 type SpanXMLScanRate =
   | R of int
   | PriceScan of float
   | PriceScanPct of float
   | VolScan of float
   | VolScanPct of float
 // Level 7
 type SpanXMLPriceScanDef =
   | Mult of float
   | Numerator of float
   | Denominator of float
 type SpanXMLVolScanDef =
   | Mult of float
   | Numerator of float
   | Denominator of float
 type SpanXMLPhy =
   | CId of int
   | Pe of string
   | P of float
   | D of float
   | V of float
   | Cvf of float
   | Val of float
   | Sc of float
   | Ra of SpanXMLRa list
 type SpanXMLGroup =
   | Id of int
   | Aval of string
 type SpanXMLEquity =
   | CId of int
   | Isin of string
   | Pe of string
   | P of float
   | D of float
   | V of float
   | Cvf of float
   | Val of float
   | Sc of float
   | Desc of string
   | Type of string
   | SubType of string
   | DivRate of SpanXMLDivRate list
   | Ra of SpanXMLRa list
 type SpanXMLUndPf =
   | Exch of string
   | PfId of int
   | PfCode of string
   | PfType of string
   | S of string
   | I of float
 type SpanXMLFut = 
   | CId of int
   | Pe of int
   | P of float
   | D of float
   | V of float
   | Cvf of float
   | Val of float
   | Sc of float
   | SetlDate of int
   | T of float
   | UndC of SpanXMLUndC list
   | Ra of SpanXMLRa list
   | ScanRate of SpanXMLScanRate list
 type SpanXMLSeries =
   | Pe of int
   | V of float
   | VolSrc of string
   | SetlDate of int
   | T of float
   | Cvf of float
   | Svf of float
   | Sc of float
   | UndC of SpanXMLUndC list
   | IntrRate of SpanXMLIntrRate list
   | DivRate of SpanXMLDivRate list
   | ScanRate of SpanXMLScanRate list
   | Opt of SpanXMLOpt list
 type SpanXMLTier =
   | Tn of int
   | EPe of int
   | SPe of int
 type SpanXMLTierWithRate =
   | Tn of int  
   | Rate of SpanXMLRate list
 type SpanXMLTierWithScanRate =
   | Tn of int  
   | SPe of int
   | EPe of int
   | ScanRate of SpanXMLScanRate list
 type SpanXMLTLeg =
   | Cc of string
   | Tn of int
   | Rs of string
   | I of float
 type SpanXMLPLeg =
   | Cc of string
   | Pe of int
   | Rs of string
   | I of float
 type SpanXMLSLeg =
   | Cc of string
   | IsTarget of int
   | IsRequired of int
 // Level 6
 type SpanXMLScanPointDef =
   | Point of int
   | PriceScanDef of SpanXMLPriceScanDef list
   | VolScanDef of SpanXMLVolScanDef list
   | Weight of float
   | PairedPoint of int
 type SpanXMLDeltaPointDef =
   | Point of int
   | PriceScanDef of SpanXMLPriceScanDef list
   | VolScanDef of SpanXMLVolScanDef list
   | Weight of float
 type SpanXMLPhyPf =
   | PfId of int
   | PfCode of string
   | Name of string
   | Currency of string
   | Cvf of float
   | PriceDl of int
   | PriceFmt of string
   | ValueMeth of string
   | PriceMeth of string
   | SetlMeth of string
   | PositionsAllowed of int
   | Phy of SpanXMLPhy list
 type SpanXMLEquityPf =
   | PfId of string
   | PfCode of string
   | Group of SpanXMLGroup list
   | Name of string
   | Currency of string
   | Cvf of float
   | PriceDl of int
   | PriceFmt of string
   | ValueMeth of string
   | PriceMeth of string
   | SetlMeth of string
   | Country of string
   | Equity of SpanXMLEquity list
 type SpanXMLFutPf =
   | PfId of string
   | PfCode of string
   | Group of SpanXMLGroup list
   | Name of string
   | Currency of string
   | Cvf of float
   | PriceDl of int
   | PriceFmt of string
   | ValueMeth of string
   | PriceMeth of string
   | SetlMeth of string
   | PositionsAllowed of int
   | UndPf of SpanXMLUndPf list
   | Fut of SpanXMLFut list
 type SpanXMLOopPf =
   | PfId of string
   | PfCode of string
   | Group of SpanXMLGroup list
   | Name of string
   | Exercise of string
   | Currency of string
   | Cvf of float
   | PriceDl of int
   | PriceFmt of string
   | StrikeDl of int
   | StrikeFmt of string
   | Cab of float
   | ValueMeth of string
   | PriceMeth of string
   | SetlMeth of string
   | PriceModel of string
   | UndPf of SpanXMLUndPf list
 type SpanXMLOofPf =
   | PfId of string
   | PfCode of string
   | Group of SpanXMLGroup list
   | Name of string
   | Exercise of string
   | Currency of string
   | Cvf of float
   | PriceDl of int
   | PriceFmt of string
   | StrikeDl of int
   | StrikeFmt of string
   | IsVariableTick of int
   | Cab of float
   | ValueMeth of string
   | PriceMeth of string
   | SetlMeth of string
   | PriceModel of string
   | UndPf of SpanXMLUndPf list
   | Series of SpanXMLSeries list
 type SpanXMLOoePf =
   | PfId of string
   | PfCode of string
   | Group of SpanXMLGroup list
   | Name of string
   | Exercise of string
   | Currency of string
   | Cvf of float
   | PriceDl of int
   | PriceFmt of string
   | StrikeDl of int
   | StrikeFmt of string
   | Cab of float
   | ValueMeth of string
   | PriceMeth of string
   | SetlMeth of string
   | PriceModel of string
   | UndPf of SpanXMLUndPf list
   | Series of SpanXMLSeries list
 type SpanXMLPfLink =
   | Exch of string
   | PfId of int
   | PfCode of string
   | PfType of string
   | Sc of float
   | CmbMeth of string
   | ApplyBasisRisk of int
   | OopDeltaMeth of string
 type SpanXMLIntraTiers =
   | Tier of SpanXMLTier  
 type SpanXMLInterTiers =
   | Tier of SpanXMLTier  
 type SpanXMLSomTiers =
   | Tier of SpanXMLTierWithRate 
 type SpanXMLRateTiers =
   | Tier of SpanXMLTierWithScanRate 
 type SpanXMLDSpread =
   | Spread of int
   | ChargeMeth of string
   | Rate of SpanXMLRate list
   | TLeg of SpanXMLTLeg list
   | PLeg of SpanXMLPLeg list
 type SpanXMLHSpread =
   | Spread of int
   | Rate of SpanXMLRate list
   | TLeg of SpanXMLTLeg list
   | SLeg of SpanXMLSLeg list
 type SpanXMLSpotRate =
   | R of int
   | Pe of int
   | Sprd of float
   | Outr of float
 // Level 5
 type SpanXMLCurConv = 
   | FromCur of string
   | ToCur of string
   | Factor of float
 type SpanXMLPbRateDef =
   | R of int
   | IsCust of int
   | AcctType of string
   | IsM of int
   | Pbc of string
 type SpanXMLPointDef =
   | R of int
   | ScanPointDef of SpanXMLScanPointDef list
   | DeltaPointDef of SpanXMLDeltaPointDef list
 type SpanXMLExchange =
   | Exch of string
   | Name of string
   | PhyPf of SpanXMLPhyPf list
   | EquityPf of SpanXMLEquityPf list
   | FutPf of SpanXMLFutPf list
   | OofPf of SpanXMLOofPf list
   | OopPf of SpanXMLOopPf list
   | OoePf of SpanXMLOoePf list
 type SpanXMLCcDef =
   | Cc of string
   | Name of string
   | Currency of string
   | RiskExponent of int
   | CapAnov of int
   | ProcMeth of string
   | WfprMeth of string
   | SpotMeth of string
   | SomMeth of string
   | CmbMeth of string
   | MarginMeth of string
   | FactorCurveSetId of int
   | FactorScenarioSetId of int
   | InterCurScan of int
   | LimitArraysTo16Points of int
   | SpotRate of SpanXMLSpotRate list
   | PfLink of SpanXMLPfLink list
   | IntraTiers of SpanXMLIntraTiers list
   | InterTiers of SpanXMLInterTiers list
   | SomTiers of SpanXMLSomTiers list
   | RateTiers of SpanXMLRateTiers list
   | IntrRate of SpanXMLIntrRate list
   | DSpread of SpanXMLDSpread list
   | Group of SpanXMLGroup list
 type SpanXMLInterSpreads =
   | DSpread of SpanXMLDSpread list
   | HSpread of SpanXMLHSpread list
 // Level 4
 type SpanXMLCurrencyDef =
   | Currency of string
   | Symbol of string
   | Name of string
   | DecimalPos of int
 type SpanXMLAcctTypeDef =
   | IsCust of int
   | AcctType of string
   | Name of string
   | IsNetMargin of int
   | Priority of int
 type SpanXMLAcctSubTypeDef =
   | AcctSubTypeCode of string
   | DataType of string
   | Description of string
 type SpanXMLGroupTypeDef =
   | Id of int
   | Name of string
 type SpanXMLGroupDef =
   | Id of int
   | Aval of string
   | Description of string
 type SpanXMLClearingOrg =
   | Ec of string
   | Name of string
   | IsContractScale of int
   | IsNetMargin of int
   | FinalizeMeth of string
   | OopDeltaMeth of string
   | CapAnov of int
   | LookAheadYears of float
   | LookAheadDays of int
   | DaysPerYear of int
   | LimitSubAccountOffset of int
   | CurConv of SpanXMLCurConv list
   | PbRateDef of SpanXMLPbRateDef list
   | PointDef of SpanXMLPointDef list
   | Exchange of SpanXMLExchange list
   | CcDef of SpanXMLCcDef list
   | InterSpreads of SpanXMLInterSpreads list
 // Level 3
 type SpanXMLDefinition = 
   | CurrencyDef of SpanXMLCurrencyDef list
   | AcctTypeDef of SpanXMLAcctTypeDef list
   | AcctSubTypeDef of SpanXMLAcctSubTypeDef list
   | GroupTypeDef of SpanXMLGroupTypeDef list
   | GroupDef of SpanXMLGroupDef list
 type SpanXMLPointInTime = 
   | Date of int
   | IsSetl of int
   | SetlQualifier of string
   | ClearingOrg of SpanXMLClearingOrg list
 // Level 2
 type SpanXMLLevel2 =
   | FileFormat of string
   | Created of int64
   | Definitions of SpanXMLDefinition list
   | PointInTime of SpanXMLPointInTime list
 // Level 1
 type SpanXMLTopLevel =
   | SpanFile of SpanXMLLevel2 list
