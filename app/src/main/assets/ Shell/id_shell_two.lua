function before(hook, param)
    log("Runtime.exec(commands) Hook Invoked")
	local arg = param:getArgument(0)
	if arg == nil then
	    log("Runtime.exec(commands) Input Argument is NULL...")
		return false
	end

	local command = param:joinArray(arg)
	if command == nil then
	    log("(commands) joined to (command) is NULL")
	    return false
	end

	log("(commands) joined to (command) value=" .. command)
	local result = param:interceptCommandArray(arg)
	if result == nil then
	    log("Command Interception returned NULL. arg=" .. command)
	    return false
	end

	if result:isMalicious() then
		log("Found Malicious Command. arg=" .. command)
	    param:setResult(result:getEchoProcess())
        return true, result:getOriginalValue(), result:getNewValue()
	end

    log("Command seems fine :> command=" .. command)
    return false
end