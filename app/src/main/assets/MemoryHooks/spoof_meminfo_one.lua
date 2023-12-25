function before(hook, param)
    --Hook version one a BEFORE Hook
    --We can replace the param to point to the TEMP File 
    --this we attack the lowest in this case libcore.io.IoBridge

    local f = param:getArgument(0)
    if f == nil then
        return false
    end

    if string.match(f, "meminfo$") then 
        local m = param:getArgument(1)
        if m ~= nil then 
            log("MEMINFO FLAGS::" .. tostring(m))
        end

        log("App is trying to Open /proc/meminfo File ... Spoofing....")

        --public File createFakeMeminfoFile(int totalGigabytes, int availableGigabytes) 
        --"settings":["memory.total", "memory.available"]

        local scope = param:getApplicationContext()
        local fake = param:getValue("MEMINFOFILE", scope)
        if fake == nil then
            local total = param:getSettingInt("memory.total", 900)
            local available = param:getSettingInt("memory.available", 500)
            fake = param:createFakeMeminfoFile(total, available)
            param:putValue("MEMINFOFILE", fake, scope)
        end

        if fake == nil then
            log("Error NIL File Object /proc/meminfo")
            return false
        end

        local path = fake:getPath()
        if path == nil then
            log("Error NIL Path Object /proc/meminfo")
            return false
        end

        log("Setting Paramter to Point to Path: (/proc/meminfo) => " .. path)
        param:setArgumentString(0, path)
        return true
    end

    return false
end
