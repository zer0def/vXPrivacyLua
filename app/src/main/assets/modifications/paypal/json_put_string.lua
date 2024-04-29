function before(hook, param)
    local a = param:getArgument(0)
    if a == nil then
        return false
    end

    local al = string.lower(a)
    if al == "gsf_id" then
        local gid = param:getSetting("unique.gsf.id")
        if gid ~= nil then
            param:setArgument(1, gid)
            return true
        end
    end


    if al == "mac_addrs" then
        local mac = param:getSetting("unique.network.mac.address")
        if mac ~= nil then
            param:setArgument(1, mac)
            return true
        end
    end

    if al == "android_id" then
        local aid = param:getSetting("unique.android.id")
        if aid ~= nil then
            param:setArgument(1, aid)
            return true
        end
    end

    local aTwo = param:getArgument(1)
    if aTwo == nil then
        return false
    end

    if al == "ef" then
        log("Bypassing Paypal Is Emulator Detection Bit Flags")
        param:setArgument(1, param:paypalFillZeros(aTwo)) --Ensure the flags for emulator is all False
        return true
    end

    if al == "rf" then
        log("Bypassing Paypal Is Root Detection Bit Flags")
        param:setArgument(1, param:paypalFillZeros(aTwo)) --Ensure the flags for Root is all False
        return true
    end

    --os_version Build.VERSION.RELEASE


    return false
end