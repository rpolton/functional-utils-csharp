module Shaftesbury.FSharp.Utils
    let first (a,b) = a
    let second (a,b) = b
    let Identity x = x
    let (|>>) value (fn1,fn2) = fn1 value, fn2 value
    //let (|>>) value fns = fns |> List.map (fun fn -> fn value)
    let applyToOption f o =
        match o with 
        | Some(value) -> Some(f value)
        | None -> None
    let readline (str:System.IO.TextReader) =
        match str.ReadLine() with
        | null -> None
        | _ as line -> Some(line, str)
    let readFrom (str:System.IO.TextReader) = Seq.unfold readline str
    let (|FirstNChars|) howMany (input:string) = input.Substring(0,howMany)
    let (|GreaterThan|) n input = input > n
    let (|GreaterThanOrEqualTo|) n input = input >= n
    let toFloat (str:string) = 
        match System.Double.TryParse str with
        | true, value -> Some(value)
        | false, _ -> None
    let toInt (str:string) = 
        match System.Int32.TryParse str with
        | true, value -> Some(value)
        | false, _ -> None
    let stringToListOfString fieldWidth s = 
        let rec toGroups s acc =
            match String.length s with
            | GreaterThanOrEqualTo fieldWidth true -> toGroups (s.Substring(fieldWidth)) ((s.Substring(0,fieldWidth))::acc)
            | _ -> acc
        toGroups s [] |> List.rev
