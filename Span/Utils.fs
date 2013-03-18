namespace Shaftesbury.FSharp

module Utils =

    let first (a,b) = a
    let second (a,b) = b

    let Identity x = x

    let between lower upper n = n>=lower && n<=upper

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

    let (|Empty|) (s:string) = System.String.IsNullOrEmpty(s)
    let (|FirstNChars|) howMany (input:string) = input.Substring(0,howMany)
    let (|GreaterThan|) n input = input > n
    let (|GreaterThanOrEqualTo|) n input = input >= n
    let (|StartsWith|) str (input:string) = input.StartsWith (str)

    let toFloat (str:string) = 
        match System.Double.TryParse str with
        | true, value -> Some(value)
        | false, _ -> None

    let toInt (str:string) = 
        match System.Int32.TryParse str with
        | true, value -> Some(value)
        | false, _ -> None

    let toInt64 (str:string) = 
        match System.Int64.TryParse str with
        | true, value -> Some(value)
        | false, _ -> None

    let stringToListOfString fieldWidth s = 
        let rec toGroups s acc =
            match String.length s with
            | GreaterThanOrEqualTo fieldWidth true -> toGroups (s.Substring(fieldWidth)) ((s.Substring(0,fieldWidth))::acc)
            | _ -> acc
        toGroups s [] |> List.rev

    let toUpper (s:string) = match s with | Empty true -> s | _ as s -> s.ToUpper()
    let toLower (s:string) = match s with | Empty true -> s | _ as s -> s.ToLower()

    let capitalise (s:string) = 
        match s with
        | Empty true -> s
        | _ as s ->
            let cs = s.ToCharArray()
            let uc = new System.String [|cs.[0]|] |> toUpper
            uc + (new System.String(Array.sub cs 1 (Array.length cs - 1)))

    let toDictionary keyFn valueFn lst =
        lst |> List.fold (fun (st:System.Collections.Generic.Dictionary<_,_>) elem -> st.Add (keyFn elem, valueFn elem) ; st) (new System.Collections.Generic.Dictionary<_,_>())

    let prepareXMLFile (filename:string) =
       let xmlDoc = new System.Xml.XmlDocument ()
       xmlDoc.Load (filename)
       let reader = new System.Xml.XmlNodeReader (xmlDoc)
       reader.MoveToContent() |> ignore
       reader

    let splitter (row:string, lengths:int list) =
        match lengths with
        | [] -> None
        | hd :: tl -> 
    //        if System.String.IsNullOrEmpty row then Some("",("",[])) else
            let edge = min hd (row.Length)
            Some (row.Substring(0,edge), // use min a b because sometimes the file seems to have fewer columns that the spec indicates
                    (row.Substring(edge),tl))

    let groupInto howMany l =
        let rec outerLoop inputlst lstlst =
        
            let rec loop lst counter acc =
                match lst with
                | [] -> [], acc |> List.rev
                | hd :: tl -> if counter < howMany then loop tl (counter + 1) (hd :: acc) else lst, acc |> List.rev

            let l'', res = loop inputlst 0 []

            match l'' with
            | [] -> res :: lstlst |> List.rev |> Seq.skip 1
            | _ -> outerLoop l'' (res :: lstlst)

        outerLoop l [[]] |> List.ofSeq
