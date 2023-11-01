# CSGOItemHandler

<p>A simple SpringBoot-TomCat Application that runs on my HomeServer. </p>

<p>It is meant to take Data about CSGOSkins and store them in a singel File and return then on Request</p>

Available EndPoints:

>/GetAllSkins
<p>Returns all Skindata with their according prices that is locally stored as JSON</p>

>/updateSkin
<p>Updates a Skins pricevalue</p>
<p>If the Skin is not doesn't exist on the Server it is added to the tracked list.</p>

>/JSON
<p>Simple TestMethod</p>

>GetAllSkinNames
<p> Returns a commaseperated Lif of all CSGO Item Hashnames</p>


