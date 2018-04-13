#!/usr/bin/env bash

trap 'exec 2> /dev/null
    rm -f $pipe
    rm -f /tmp/init_env_state
    rm -f /tmp/runtime_env_state
    kill $print_pid
    kill -- -$target_pid' EXIT

#####  em: $$ is the process ID (PID) of the script itself.
pipe=/tmp/pipe_$$
mkfifo $pipe

#####  Check usage

if [ ${#@} -eq 0 ]; then
    echo
    echo Usage: "$(basename $0)" command arg1 arg2 ...
    echo
    exit 1
else
    target_command=$1
    ##### em: shift is a bash built-in which kind of removes arguments
    shift
fi

#####  Trace functions

__trap_debug__() {
    (set -o posix ; set) > /tmp/runtime_env_state
    set -o monitor
    suspend -f
    set +o monitor
}

__trace_ON__() {
    (set -o posix ; set) > /tmp/init_env_state
    set -o xtrace -o functrace
    trap __trap_debug__ DEBUG
}
__trace_OFF__() {
    trap - DEBUG
    set +o xtrace +o functrace
}




export -f __trace_ON__ __trace_OFF__ __trap_debug__


#####  Prompt for xtrace

export PS4='\[\e[0;32m\]${BASH_SOURCE##*/} line \[\e[0;49;95m\]$((${LINENO}-1)): \[\e[0;32m\]${FUNCNAME[0]:+${FUNCNAME[0]}(): }\[\e[0m\]'


#####  Read from pipe and print xtrace

while read -r line; do
    case $line in
        *__trace_OFF__* )  continue ;;
        *__trap_debug__* )  continue ;;
    esac
    echo "$line" >& 2
done < $pipe &

print_pid=$!


#####  Excute target command

# disable suspend
set -o monitor

# enable main for shell functions
bash -c "$target_command"' "$0" "$@"' "$@" &> $pipe &

target_pid=$!


#####  Trace !

while read line; do

    if [ "$line" == "help" ]
    then
        printf "\n\033[0;32m======================================== SCRIPT ENVIRONMENT ========================================\033[0m\n"
        cat /tmp/runtime_env_state
        printf "\033[0;32m====================================================================================================\033[0m\n\n"
        continue
    fi


    if [ "$line" == "env -f" ]
    then
        printf "\n\033[0;32m======================================== SCRIPT ENVIRONMENT ========================================\033[0m\n"
        cat /tmp/runtime_env_state
        printf "\033[0;32m====================================================================================================\033[0m\n\n"
        continue
    fi

    if [ "$line" == "env" ]
    then
        sed -i '' '/^BASH_LINENO/d' /tmp/runtime_env_state
        sed -i '' '/^LINENO/d' /tmp/runtime_env_state
        sed -i '' '/^PS4/d' /tmp/runtime_env_state
        sed -i '' '/^SHELLOPTS/d' /tmp/runtime_env_state
        sed -i '' '/^FUNCNAME/d' /tmp/runtime_env_state
        printf "\n\033[0;34m======================================== ENVIRONMENT ========================================\n"
        grep -Fxvf /tmp/init_env_state /tmp/runtime_env_state
        printf "=============================================================================================\033[0m\n"
        continue
    fi

    if [ "$line" == "exit" ]
    then
        exit
    fi

    if kill -0 $target_pid 2> /dev/null; then
        fg %% > /dev/null
    else
        exit
    fi
done
