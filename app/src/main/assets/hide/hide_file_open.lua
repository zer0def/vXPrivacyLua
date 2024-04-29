function before(hook, param)
	local fil = param:getArgument(0)
	if fil == nil then
		return false
	end

	if param:fileIsEvidence(fil, 3) then
		log("Libcore.io is [open] Environment Directory!")
		local clsFileNotFound = luajava.bindClass("java.io.FileNotFoundException")
		local fake = luajava.new(clsFileNotFound, fil)
		param:setResult(fake)
		return true, fil
	end

	return false
end