function before(hook, param)
    local lat = param:getSetting("location.latitude")
    local log = param:getSetting("location.longitude")
    local rad = param:getSetting("location.radius")

    if lat == nil or log == nil or rad == nil then
        lat = 0
        log = 0
        rad = 0.1
    end

    param:setArgument(0, lat) -- latitude
    param:setArgument(1, log) -- longitude
    param:setArgument(2, rad) -- radius
    return true
end
