function after(hook, param)
	local fil = param:getArgument(0)
	if fil == nil then 
		return false
	end

	if param:fileIsEvidence(fil, 3) then
		log("Libcore.io [access] Root Emulator Directory!")
		param:setResult(false)
		return true
	end

	return false
end