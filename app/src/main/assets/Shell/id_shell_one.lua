function before(hook, param)
    log("Runtime.exec(command) Hook Invoked")
	local arg = param:getArgument(0)
	if arg == nil then
	    log("Runtime.exec(command) input argument is NULL...")
		return false
	end

    log("Runtime.exec(" .. arg .. ")")
	local comRes = param:interceptCommand(arg)
	if comRes == nil then
        log("Command Interception returned NULL. Command is Clean! arg=" .. arg)
		return false
	end

    log("Command was intercepted=" .. arg .. " !!")
	return true, arg, comRes
end