namespace Shaftesbury.Span.XML2.ParserDataTypes

 // Level 9;
type SpanXMLDiv = {
    Val : float;
    Dtm : int;
    SetlDate : int;
    }
 // Level 8;
type SpanXMLRa = {
    R : int;
    A : float list;
    D : float;
    }
type SpanXMLDivRate = {
    Val : float;
//    Div : SpanXMLDiv list;
    }
type SpanXMLUndC = {
    Exch : string;
    PfId : int;
    CId : int;
    S : string;
    I : float;
    }
type SpanXMLIntrRate = {
    Val : float;
    Rl : int;
    Cpm : int;
    Exm : int;
    }
type SpanXMLOpt = {
    CId : int;
    O : string;
    K : float;
    P : float;
    Pq : int;
    D : float;
    V : float;
    Val : float;
    Cvf : float;
    Svf : float;
//    Ra : SpanXMLRa list;
    }
type SpanXMLRate = {
    R : int;
    Val : float;
    }
type SpanXMLScanRate = {
    R : int;
    PriceScan : float;
    PriceScanPct : float;
    VolScan : float;
    VolScanPct : float;
    }
 // Level 7;
type SpanXMLPriceScanDef = {
    Mult : float;
    Numerator : float;
    Denominator : float;
    }
type SpanXMLVolScanDef = {
    Mult : float;
    Numerator : float;
    Denominator : float;
    }
type SpanXMLPhy = {
    CId : int;
    Pe : string;
    P : float;
    D : float;
    V : float;
    Cvf : float;
    Val : float;
    Sc : float;
    //Ra : SpanXMLRa list;
    }
type SpanXMLGroup = {
    Id : int;
    Aval : string;
    }
type SpanXMLEquity = {
    CId : int;
    Isin : string;
    Pe : string;
    P : float;
    D : float;
    V : float;
    Cvf : float;
    Val : float;
    Sc : float;
    Desc : string;
    Type : string;
    SubType : string;
//    DivRate : SpanXMLDivRate list;
//    Ra : SpanXMLRa list;
    }
type SpanXMLUndPf = {
    Exch : string;
    PfId : int;
    PfCode : string;
    PfType : string;
    S : string;
    I : float;
    }
type SpanXMLFut = {
    CId : int;
    Pe : int;
    P : float;
    D : float;
    V : float;
    Cvf : float;
    Val : float;
    Sc : float;
    SetlDate : int;
    T : float;
//    UndC : SpanXMLUndC list;
//    Ra : SpanXMLRa list;
//    ScanRate : SpanXMLScanRate list;
    }
type SpanXMLSeries = {
    Pe : int;
    V : float;
    VolSrc : string;
    SetlDate : int;
    T : float;
    Cvf : float;
    Svf : float;
    Sc : float;
//    UndC : SpanXMLUndC list;
//    IntrRate : SpanXMLIntrRate list;
//    DivRate : SpanXMLDivRate list;
//    ScanRate : SpanXMLScanRate list;
//    Opt : SpanXMLOpt list;
    }
type SpanXMLTier = {
    Tn : int;
    EPe : int option;
    SPe : int option;
    Tne : int option;
    Tbn : int option;
    Btn : int option;
    Brk : int option;
//    Rate : SpanXMLRate list;
//    ScanRate : SpanXMLScanRate list;
    }
type SpanXMLTLeg = {
    Cc : string;
    Tn : int;
    Rs : string;
    I : float;
    }
type SpanXMLPLeg = {
    Cc : string;
    Pe : int;
    Rs : string;
    I : float;
    }
type SpanXMLSLeg = {
    Cc : string;
    IsTarget : int;
    IsRequired : int;
    }
 // Level 6;
type SpanXMLScanPointDef = {
    Point : int;
//    PriceScanDef : SpanXMLPriceScanDef list;
//    VolScanDef : SpanXMLVolScanDef list;
    Weight : float;
    PairedPoint : int;
    }
type SpanXMLDeltaPointDef = {
    Point : int;
//    PriceScanDef : SpanXMLPriceScanDef list;
//    VolScanDef : SpanXMLVolScanDef list;
    Weight : float;
    }
type SpanXMLPhyPf = {
    PfId : int;
    PfCode : string;
    Name : string;
    Currency : string;
    Cvf : float;
    PriceDl : int;
    PriceFmt : string;
    ValueMeth : string;
    PriceMeth : string;
    SetlMeth : string;
    PositionsAllowed : int;
//    Phy : SpanXMLPhy list;
    }
type SpanXMLEquityPf = {
    PfId : string;
    PfCode : string;
//    Group : SpanXMLGroup list;
    Name : string;
    Currency : string;
    Cvf : float;
    PriceDl : int;
    PriceFmt : string;
    ValueMeth : string;
    PriceMeth : string;
    SetlMeth : string;
    Country : string;
//    Equity : SpanXMLEquity list;
    }
type SpanXMLFutPf = {
    PfId : string;
    PfCode : string;
//    Group : SpanXMLGroup list;
    Name : string;
    Currency : string;
    Cvf : float;
    PriceDl : int;
    PriceFmt : string;
    ValueMeth : string;
    PriceMeth : string;
    SetlMeth : string;
    PositionsAllowed : int;
//    UndPf : SpanXMLUndPf list;
//    Fut : SpanXMLFut list;
    }
type SpanXMLOopPf = {
    PfId : string;
    PfCode : string;
//    Group : SpanXMLGroup list;
    Name : string;
    Exercise : string;
    Currency : string;
    Cvf : float;
    PriceDl : int;
    PriceFmt : string;
    StrikeDl : int;
    StrikeFmt : string;
    Cab : float;
    ValueMeth : string;
    PriceMeth : string;
    SetlMeth : string;
    PriceModel : string;
//    UndPf : SpanXMLUndPf list;
    }
type SpanXMLOofPf = {
    PfId : string;
    PfCode : string;
//    Group : SpanXMLGroup list;
    Name : string;
    Exercise : string;
    Currency : string;
    Cvf : float;
    PriceDl : int;
    PriceFmt : string;
    StrikeDl : int;
    StrikeFmt : string;
    Cab : float;
    ValueMeth : string;
    PriceMeth : string;
    SetlMeth : string;
    PriceModel : string;
    IsVariableTick : int;
//    UndPf : SpanXMLUndPf list;
//    Series : SpanXMLSeries list;
    }
type SpanXMLOoePf = {
    PfId : string;
    PfCode : string;
//    Group : SpanXMLGroup list;
    Name : string;
    Exercise : string;
    Currency : string;
    Cvf : float;
    PriceDl : int;
    PriceFmt : string;
    StrikeDl : int;
    StrikeFmt : string;
    Cab : float;
    ValueMeth : string;
    PriceMeth : string;
    SetlMeth : string;
    PriceModel : string;
//    UndPf : SpanXMLUndPf list;
//    Series : SpanXMLSeries list;
    }
type SpanXMLPfLink = {
    Exch : string;
    PfId : int;
    PfCode : string;
    PfType : string;
    Sc : float;
    CmbMeth : string;
    ApplyBasisRisk : int;
    OopDeltaMeth : string;
    }
type SpanXMLIntraTiers = struct end
//    Tier : SpanXMLTier  ;
    
type SpanXMLInterTiers = struct end
//    Tier : SpanXMLTier  ;
    
type SpanXMLSomTiers = struct end
//    Tier : SpanXMLTier ;
    
type SpanXMLRateTiers = struct end
//    Tier : SpanXMLTier ;
    
type SpanXMLDSpread = {
    Spread : int;
    ChargeMeth : string;
//    Rate : SpanXMLRate list;
//    TLeg : SpanXMLTLeg list;
//    PLeg : SpanXMLPLeg list;
    }
type SpanXMLHSpread = {
    Spread : int;
//    Rate : SpanXMLRate list;
//    TLeg : SpanXMLTLeg list;
//    SLeg : SpanXMLSLeg list;
    }
type SpanXMLSpotRate = {
    R : int;
    Pe : int;
    Sprd : float;
    Outr : float;
    }
 // Level 5;
type SpanXMLCurConv = {
    FromCur : string;
    ToCur : string;
    Factor : float;
    }
type SpanXMLPbRateDef = {
    R : int;
    IsCust : int;
    AcctType : string;
    IsM : int;
    Pbc : string;
    }
type SpanXMLPointDef = {
    R : int;
//    ScanPointDef : SpanXMLScanPointDef list;
//    DeltaPointDef : SpanXMLDeltaPointDef list;
    }
type SpanXMLExchange = {
    Exch : string;
    Name : string;
//    PhyPf : SpanXMLPhyPf list;
//    EquityPf : SpanXMLEquityPf list;
//    FutPf : SpanXMLFutPf list;
//    OofPf : SpanXMLOofPf list;
//    OopPf : SpanXMLOopPf list;
//    OoePf : SpanXMLOoePf list;
    }
type SpanXMLCcDef = {
    Cc : string;
    Name : string;
    Currency : string;
    RiskExponent : int;
    CapAnov : int;
    ProcMeth : string;
    WfprMeth : string;
    SpotMeth : string;
    SomMeth : string;
    CmbMeth : string;
    MarginMeth : string;
    FactorCurveSetId : int;
    FactorScenarioSetId : int;
    InterCurScan : int;
    LimitArraysTo16Points : int;
//   SpotRate : SpanXMLSpotRate list;
//    PfLink : SpanXMLPfLink list;
//    IntraTiers : SpanXMLIntraTiers list;
//    InterTiers : SpanXMLInterTiers list;
//    SomTiers : SpanXMLSomTiers list;
//    RateTiers : SpanXMLRateTiers list;
//    IntrRate : SpanXMLIntrRate list;
//    DSpread : SpanXMLDSpread list;
//    Group : SpanXMLGroup list;
    }
type SpanXMLInterSpreads = struct end
//    DSpread : SpanXMLDSpread list;
//    HSpread : SpanXMLHSpread list;
    
 // Level 4;
type SpanXMLCurrencyDef = {
    Currency : string;
    Symbol : string;
    Name : string;
    DecimalPos : int;
    }
type SpanXMLAcctTypeDef = {
    IsCust : int;
    AcctType : string;
    Name : string;
    IsNetMargin : int;
    Priority : int;
    }
type SpanXMLAcctSubTypeDef = {
    AcctSubTypeCode : string;
    DataType : string;
    Description : string;
    }
type SpanXMLGroupTypeDef = {
    Id : int;
    Name : string;
    }
type SpanXMLGroupDef = {
    Id : int;
    Aval : string;
    Description : string;
    }
type SpanXMLClearingOrg = {
    Ec : string;
    Name : string;
    IsContractScale : int;
    IsNetMargin : int;
    FinalizeMeth : string;
    OopDeltaMeth : string;
    CapAnov : int;
    LookAheadYears : float;
    LookAheadDays : int;
    DaysPerYear : int;
    LimitSubAccountOffset : int;
//    CurConv : SpanXMLCurConv list;
//    PbRateDef : SpanXMLPbRateDef list;
//    PointDef : SpanXMLPointDef list;
//    Exchange : SpanXMLExchange list;
//    CcDef : SpanXMLCcDef list;
//    InterSpreads : SpanXMLInterSpreads list;
    }
 // Level 3;
type SpanXMLDefinition = struct end
//    CurrencyDef : SpanXMLCurrencyDef list;
//    AcctTypeDef : SpanXMLAcctTypeDef list;
//    AcctSubTypeDef : SpanXMLAcctSubTypeDef list;
//    GroupTypeDef : SpanXMLGroupTypeDef list;
//    GroupDef : SpanXMLGroupDef list;
    
type SpanXMLPointInTime = {
    Date : int;
    IsSetl : int;
    SetlQualifier : string;
//    ClearingOrg : SpanXMLClearingOrg list;
    }
 // Level 2;
type SpanXMLLevel2 = {
    FileFormat : string;
    Created : int64;
//    Definitions : SpanXMLDefinition list;
//    PointInTime : SpanXMLPointInTime list;
    }
 // Level 1;
type SpanXMLTopLevel = struct end
// SpanFile : SpanXMLLevel2 list

type nodeType =
    | SpanXMLDiv of SpanXMLDiv
    | SpanXMLRa of SpanXMLRa
    | SpanXMLDivRate of SpanXMLDivRate
    | SpanXMLUndC of SpanXMLUndC
    | SpanXMLIntrRate of SpanXMLIntrRate
    | SpanXMLOpt of SpanXMLOpt
    | SpanXMLRate of SpanXMLRate
    | SpanXMLScanRate of SpanXMLScanRate
    | SpanXMLPriceScanDef of SpanXMLPriceScanDef
    | SpanXMLVolScanDef of SpanXMLVolScanDef
    | SpanXMLPhy of SpanXMLPhy
    | SpanXMLGroup of SpanXMLGroup
    | SpanXMLEquity of SpanXMLEquity
    | SpanXMLUndPf of SpanXMLUndPf
    | SpanXMLFut of SpanXMLFut
    | SpanXMLSeries of SpanXMLSeries
    | SpanXMLTier of SpanXMLTier
    | SpanXMLTLeg of SpanXMLTLeg
    | SpanXMLPLeg of SpanXMLPLeg
    | SpanXMLSLeg of SpanXMLSLeg
    | SpanXMLScanPointDef of SpanXMLScanPointDef
    | SpanXMLDeltaPointDef of SpanXMLDeltaPointDef
    | SpanXMLPhyPf of SpanXMLPhyPf
    | SpanXMLEquityPf of SpanXMLEquityPf
    | SpanXMLFutPf of SpanXMLFutPf
    | SpanXMLOopPf of SpanXMLOopPf
    | SpanXMLOofPf of SpanXMLOofPf
    | SpanXMLOoePf of SpanXMLOoePf
    | SpanXMLPfLink of SpanXMLPfLink
    | SpanXMLIntraTiers of SpanXMLIntraTiers
    | SpanXMLInterTiers of SpanXMLInterTiers
    | SpanXMLSomTiers of SpanXMLSomTiers
    | SpanXMLRateTiers of SpanXMLRateTiers
    | SpanXMLDSpread of SpanXMLDSpread
    | SpanXMLHSpread of SpanXMLHSpread
    | SpanXMLSpotRate of SpanXMLSpotRate
    | SpanXMLCurConv of SpanXMLCurConv
    | SpanXMLPbRateDef of SpanXMLPbRateDef
    | SpanXMLPointDef of SpanXMLPointDef
    | SpanXMLExchange of SpanXMLExchange
    | SpanXMLCcDef of SpanXMLCcDef
    | SpanXMLInterSpreads of SpanXMLInterSpreads
    | SpanXMLCurrencyDef of SpanXMLCurrencyDef
    | SpanXMLAcctTypeDef of SpanXMLAcctTypeDef
    | SpanXMLAcctSubTypeDef of SpanXMLAcctSubTypeDef
    | SpanXMLGroupTypeDef of SpanXMLGroupTypeDef
    | SpanXMLGroupDef of SpanXMLGroupDef
    | SpanXMLClearingOrg of SpanXMLClearingOrg
    | SpanXMLDefinition of SpanXMLDefinition
    | SpanXMLPointInTime of SpanXMLPointInTime
    | SpanXMLLevel2 of SpanXMLLevel2
    | SpanXMLTopLevel of SpanXMLTopLevel

type tree =
    | Node of nodeType * tree list
