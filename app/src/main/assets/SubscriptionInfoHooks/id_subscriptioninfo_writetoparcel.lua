function before(hook, param)
	local dp = param:getArgument(0)
	local fl = param:getArgument(1)
	local ts = param:getThis()

	if dp == nil or ts == nil or fl == nil then
		return false
	end

	log("Spoofing SubscriptionInfo.writeToParcel")
	local code = param:getSDKCode()
    log("Android SDK:" .. code)

	dp:writeInt(ts:getSubscriptionId())			--mId
	dp:writeString(ts:getIccId())				--mIccId
    dp:writeInt(ts:getSimSlotIndex())			--mSimSlotIndex

    if code > 30 then
    	--11.0 and Higher they use TextUtils to Write it
    	local tx_utils = luajava.bindClass("android.text.TextUtils")
    	tx_utils:writeToParcel(ts:getDisplayName(), dp, 0)
		tx_utils:writeToParcel(ts:getCarrierName(), dp, 0)
    else
    	--10.0 and lower
    	dp:writeCharSequence(ts:getDisplayName())	--mDisplayName
    	dp:writeCharSequence(ts:getCarrierName())	--mCarrierName
    end

    if code >= 34 then
    	--14.0 and Higher
    	dp:writeInt(ts:getDisplayNameSource())		--mDisplayNameSource -> getDisplayNameSource()
    else
    	--13.0 lower
    	dp:writeInt(ts:getNameSource())				--mNameSource -> getNameSource()
    end

    dp:writeInt(ts:getIconTint())				--mIconTint
    dp:writeString(ts:getNumber())				--mNumber
    dp:writeInt(ts:getDataRoaming())			--mDataRoaming

    if code > 28 then
    	--10.0 and Higher
    	dp:writeString(ts:getMccString())					--mMcc
    	dp:writeString(ts:getMncString())					--mMnc
    else
    	--9.0 and lower they write it as int
    	dp:writeInt(ts:getMcc())					--mMcc
    	dp:writeInt(ts:getMnc())					--mMnc
    end

    dp:writeString(ts:getCountryIso())			--mCountryIso

    if code <= 28 then
    	--9.0 and lower
    	ts.mIconBitmap:writeToParcel(dp, fl)
    elseif code >= 29 and code < 33 then
    	--10.0 - 12.0
    	--13.0+ they remove and use lazy loading
    	dp:writeParcelable(ts.mIconBitmap, fl)
    end


    if code >= 27 then
    	--8.1 and Higher
    	dp:writeBoolean(ts:isEmbedded())				--mIsEmbedded
        dp:writeTypedArray(ts.mNativeAccessRules, fl)
    end

    if code == 28 then
    	--9.0
        dp:writeString(ts:getCardId()) 				--mCardId
    elseif code > 28 then
    	--10.0 and Higher
    	dp:writeString(ts:getCardString())			--mCardString
    	dp:writeInt(ts:getCardId())					--mCardId
    end

    if code >= 33 then
    	--13.0 Higher insert
    	dp:writeInt(ts:getPortIndex())
    end

    if code > 28 then
    	--10.0 -> 14.0
    	--14 it changes field name from [mSubscriptionType] to [mType]
    	dp:writeBoolean(ts:isOpportunistic())		--mIsOpportunistic
    	if ts.mGroupUuid == nil then
        	dp:writeString8(null)
        else
        	--shall we spoof this ?
        	dp:writeString8(ts.mGroupUuid:toString())
        end

        dp:writeBoolean(ts:isGroupDisabled())		--mIsGroupDisabled
        dp:writeInt(ts:getCarrierId())				--mCarrierId
        dp:writeInt(ts:getProfileClass())			--mProfileClass
        dp:writeInt(ts:getSubscriptionType())		--[13 less][mSubscriptionType] , [14 higher][mType]
        dp:writeStringArray(ts:getEhplmns())		--mEhplmns
        dp:writeStringArray(ts:getHplmns())			--mHplmns
        dp:writeString(ts:getGroupOwner())			--mGroupOwner
    end

    if code >= 30 then
    	--11.0 higher we tack this stuff on
    	dp:writeTypedArray(ts.mCarrierConfigAccessRules, fl)
        dp:writeBoolean(ts:areUiccApplicationsEnabled())		--mAreUiccApplicationsEnabled
    end

    if code >= 33 then
    	--13.0 and higher
    	dp:writeInt(ts:getUsageSetting())						--mUsageSetting
    end

	return true
end

