function before(hook, param)
	local arg = param:getArgument(0)
	if arg == nil then
		return false
	end

    log("[xlog] Runtime.exec(command) => " .. arg)
    return false
end