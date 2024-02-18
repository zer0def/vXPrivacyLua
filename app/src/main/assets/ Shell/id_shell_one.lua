function before(hook, param)
    log("Runtime.exec(command) Hook Invoked")
	local arg = param:getArgument(0)
	if arg == nil then
	    log("Runtime.exec(command) input argument is NULL...")
		return false
	end

    log("Runtime.exec(" .. arg .. ")")
	local result = param:interceptCommand(arg)
	if result == nil then
        log("Command Interception returned NULL. arg=" .. arg)
		return false
	end

	if result:isMalicious() then
	    log("Found Malicious command. arg=" .. arg)
	    param:setResult(result:getEchoProcess())
	    return true, result:getOriginalValue(), result:getNewValue()
	end
	log("Command is clean! arg=" .. arg)
	return false
end