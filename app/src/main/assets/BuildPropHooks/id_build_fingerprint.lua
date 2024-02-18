function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.fingerprint", "OnePlus/yaap_guacamoles/guacamoles:13/TQ3A.230805.001/ido10031738:user/release-keys")
    param:setResult(fake)
    return true, ret, fake
end