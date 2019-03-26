#!/bin/bash

dic=( $(cat ~/Downloads/61000/dic.txt) )

textByWords=( $( tr -cs '[:alpha:]' '\n' < ~/Downloads/61000/text.txt | awk '{print tolower($0)}' | sort | uniq ) )

num=100
dicIndex=0

for (( i=0; i<${#textByWords[@]}; i++ ))
do
    for (( j=0; j<${#dic[@]}; j++ ))
    do
        temp=$( levenshtein ${dic[j]} ${textByWords[i]} )
        if [[ ${num} -gt ${temp} ]]; then
            dicIndex=${j}
            num=${temp}
        fi
    done

    if [[ ${num} != 0 ]]; then
        echo ${textByWords[i]}:${num}:${dic[dicIndex]}
    fi

    num=100
done

echo -------------

tr -cs '[:alpha:]' '\n' < ~/Downloads/61000/text.txt | awk '{print tolower($0)}' | sort | uniq | xargs -I{} -n1 sh -c '(echo {}: && agrep -w -B -s {} ~/Downloads/61000/dic.txt) | tr "\n" ":"; echo "\n"'| grep . | awk -F: '{if ($3 != 0) print $1":"$3":"$4}'

