function before(h, param)

    --use
    local this = param:getThis()
    if this == nil then
        return false
    end

    local hooked = param:getValue('hooked', this)
    if hooked then
        return false
    else
        param:putValue('hooked', true, this)
    end

    local settings = this:getSettings()
    if settings == nil then
        return false
    else
        --public static Globals getGlobals
        --DebugLib = 	void callHook(LuaThread.State s, LuaValue type, LuaValue arg)
        --local ua = 'Mozilla/5.0 (Linux; U; Android; en-us) AppleWebKit/999+ (KHTML, like Gecko) Safari/999.9'
        local ua = param:getSetting("user.agent")
        if ua == nil then
            return false
        end

        --hook is a Global var declared
        --(1) arg is the class
        --(2) arg is the method
        --(3) arg is the Function that will be invoked like a call back
        --(4) arg is the Value the fake arg to hand it
        --LuaHook.java is where it gets handed off to
        hook(settings, 'setUserAgentString', setUserAgentString, ua)
        settings:setUserAgentString('dummy')
        return true
    end
end

function setUserAgentString(when, param, ua)
    if when == 'before' then
        if param:getArgument(0) ~= ua then
            log('Setting ua=' .. ua)
            param:setArgument(0, ua)
        end
    end
end
