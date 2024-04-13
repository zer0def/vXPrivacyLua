function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local p = param:getArgument(0)
    if p == nil then
        return false
    end

    log("GPU GETSTRING: " .. p)
    if p == 7936 then
        local vendor = param:getSetting("gpu.gl.vendor")
        if vendor == nil then
            return false
        end

        param:setResult(vendor)
        return true, res, vendor
    end

    if p == 7937 then
        local ren = param:getSetting("gpu.gl.renderer")
        if ren == nil then
            return false
        end

        param:setResult(ren)
        return true, res, ren
    end

    if p == 7938 then
        local ver = param:getSetting("gpu.gl.version")
        if ver == nil then
            return false
        end

        param:setResult(ver)
        return true, res, ver
    end

    return false
end