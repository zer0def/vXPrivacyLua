function after(h, param)
    local this = param:getThis()
    if this == nil then
        return false
    end

    local ua = param:getSetting("user.agent")
    if ua == nil then
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
        -- "Stripe/v1 ".concat("AndroidBindings/20.25.5");
        -- userAgentString = userAgentString == null ? "" : userAgentString;
        -- settings.setUserAgentString(userAgentString + " [" + concat + "]");
        local agent = ua .. " [Stripe/v1 AndroidBindings/20.25.5]"
        hook(settings, 'setUserAgentString', setUserAgentString, agent)
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
