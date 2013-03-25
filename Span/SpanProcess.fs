namespace Shaftesbury.Span

module SpanProcess =

    open Shaftesbury.FSharp.Utils

    // Given a list of positions and an ordered list of possible matches (of the form 1 of these corresponds to 0.75 of those, for example)
    // return the reduced list of positions having applied the matches

    let findNonOffsettingInsts (portfolio: (_ * float) list) matchingSet =
        let findReduction pf removeThis =
            // proportionsToRemove and pfPosnsToReduce should be in the same order
            let proportionsToRemove = removeThis |> List.sortBy first |> List.map second
            let pfWithout = pf |> List.filter (fun (inst,_) -> removeThis |> List.forall (fun (inst1,_) -> inst <> inst1))
            let pfPosnsToReduce = pf |> List.filter (fun (inst,_) -> removeThis |> List.exists (fun (inst1,_) -> inst = inst1)) |> List.sortBy first
            // Find the absolute size of the positions
            let pfSigns, pfAbsQuantities = pfPosnsToReduce |> List.map (fun (_,q) -> (if q<0.0 then -1.0 else 1.0), abs q) |> List.unzip

            let reduction = reduce pfAbsQuantities proportionsToRemove

            let reductionWithInsts = List.zip reduction pfPosnsToReduce |> List.map (fun (newVal, (inst,q)) -> inst, newVal)
            let reductionSignedWithInsts = List.zip reductionWithInsts pfSigns |> List.map (fun ((inst,q),b) -> inst, q*b)
            reductionSignedWithInsts, pfWithout @ (List.zip pfPosnsToReduce reductionSignedWithInsts |> List.map (fun ((inst,q),(rInst,rQ)) -> inst, q-rQ))

        let rec findInst portfolio matchingSet acc =
            match matchingSet with
            | [] -> portfolio, (acc |> List.rev)
            | lst :: tl ->
                let matchCandidates = lst |> List.map first |> Set.ofList
                let pfCodes = portfolio |> List.filter (fun (inst,q) -> q<>0.0) |> List.map first |> Set.ofList
                if Set.isSubset matchCandidates pfCodes then // if all matchCandidates are in pfCodes then
                    let reduction, reducedPortfolio = findReduction portfolio lst
                    findInst reducedPortfolio tl (reduction :: acc)
                else
                    findInst portfolio tl acc

        findInst portfolio matchingSet []


