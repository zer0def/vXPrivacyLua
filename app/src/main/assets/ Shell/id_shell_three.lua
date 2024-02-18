function before(hook, param)
    log("ProcessBuilder.start() hook Invoked")
	local ths = param:getThis()
	if ths == nil then
	    log("ProcessBuilder.start() (this) context is NULL")
		return false
	end

	local commands = ths:command()
	if commands == nil then
	    log("(commands) is NULL")
		return false
	end

	local command = param:joinList(commands)
	if command == nil then
	    log("(commands) joined into (command) is NULL")
	    return false
	end

    log("ProcessBuilder.start() (commands) joined into (command) =" .. command)
	local result = param:interceptCommandList(commands)
	if result == nil then
	    log("Result from (" .. command .. ") returned NULL ...")
	    return false
	end

	if result:isMalicious() then
	    log("Found Malicious Command. arg=" .. command)
	    param:setResult(result:getEchoProcess())
        return true, result:getOriginalValue(), result:getNewValue()
	end

	log("Command (" .. command .. ") looks clean!")
    return false
end