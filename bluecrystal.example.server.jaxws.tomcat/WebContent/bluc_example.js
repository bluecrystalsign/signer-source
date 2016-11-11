	var provider = "";
	var mapProviders = 
		{
			'restSigner':{
				'isActive':false,
				'description':'<h5>Requisitos:<br/>Windows, Internet Explorer e a instalacao do RestSigner./h5>'
			},
			'chromeExtension':{
				'isActive':false,
				'description':'<h5>Requisitos:<br/>Windows, Chrome e a instalacao da extensão no Chrome e no componente Native Messaging.</h6>'
			},
			'appletCapi':{
				'isActive':false,
				'description':'<h5>Requisitos:<br/>Windows, Firefox e a instalacao do Java no navegador.'
					+"<br/><a href='https://www.java.com/en/download/installed.jsp' target=_blank>Teste aqui o funcionamento do Java no seu navegador</a></h5>"
			},
			'appletP11':{
				'isActive':false,
				'description':'<h5>Requisitos:<br/>Firefox, driver PKCS#11 do cartão / token instalado (se aplicavel) e a instalacao do Java no navegador.'
					+"<br/><a href='https://www.java.com/en/download/installed.jsp' target=_blank>Teste aqui o funcionamento do Java no seu navegador</a></h5>"
			},
			'activeX':{
				'isActive':false,
				'description':'<h5>Requisitos:<br/>Windows, Internet Explorer e a instalacao do ActiveX./h5>'
			}
		};
	var statusMessage = 
		{
			"-1":"Aconteceu um erro",
			"0":"Assinatura valida",
			"1":"No momento não existem dados para validar o certificado",
			"2":"O certificado está expirado",
			"3":"Não foi possivel construir o caminho de certificacao",
			"4":"Não foi possível processar a resposta (erro interno)",
			"100":"O certificado está revogado (seja permananentemente ou temporariamente suspenso)",
			"101":"A chave privada do certificado foi comprometida",
			"102":"A chave privada da AC do certificado foi comprometida",
			"103":"Troca de afiliacao",
			"104":"O certificado foi substituido",
			"105":"A operaçao da AC foi encerrada",
			"106":"Certificado suspenso",
			"108":"Certificado foi removido da LCR",
			"109":"O privilégio foi removido",
			"110":"A Autoridade de Atributo foi comprometida",
			"1000":"A chave não pode ser usada",
			"1001":"A cadeia não é confiável",
			"9999":"O documento foi modificado após a assinatura"
				}


window.onload = function() {
		var dropbox = document.getElementById("dropbox");
		dropbox.addEventListener("dragenter", noop, false);
		dropbox.addEventListener("dragexit", noop, false);
		dropbox.addEventListener("dragover", noop, false);
		dropbox.addEventListener("drop", dropUpload, false);
	}

	function noop(event) {
		event.stopPropagation();
		event.preventDefault();
	}

	function dropUpload(event) {
		noop(event);
		var files = event.dataTransfer.files;

		for (var i = 0; i < files.length; i++) {
			upload(files[i]);
		}
	}

	function upload(file) {
		document.getElementById("status").innerHTML = "Uploading " + file.name;

		var formData = new FormData();
		formData.append("file", file);

		var xhr = new XMLHttpRequest();
		xhr.upload.addEventListener("progress", uploadProgress, false);
		xhr.addEventListener("load", uploadComplete, false);
		xhr.open("POST", "uploadServlet", true); // If async=false, then you'll miss progress bar support.
		xhr.send(formData);
	}

	function uploadProgress(event) {
		// Note: doesn't work with async=false.
		var progress = Math.round(event.loaded / event.total * 100);
		document.getElementById("status").innerHTML = "Progress " + progress
				+ "%";
	}

	function uploadComplete(event) {
		//document.getElementById("status").innerHTML = event.target.responseText;        	 	

		showSucess(event.target.responseText);
        var callMethod = provider+"_assinar";
        window[callMethod]();

	}

	
	function showCxExtensionAlert() {
		showWarning(
				'Parece que seu navegador ainda não tem a extensão Blue Crystal '
				+'instalada. <a '
				+'href="https://chrome.google.com/webstore/detail/blue-crystal-signer/inlgdajmhicinhamnepnpdneamfgjcgl?hl=pt-BR&authuser=2">'
					+'Antes de iniciar o processo instale a entensao.</a>'		
		);
	}

	function showCxNativeAlert() {
		showWarning(
				'Parece que seu computador ainda não tem o componente nativo Blue '
				+ 'Crystal instalada. <a id="msg_install_ext_native"'
				+ 'href="https://github.com/bluecrystalsign/signer-source-vstudio/tree/master/dist">Instale '
					+"o 'native messaging' daqui</a>"
				);
	}
	
	function showRsExtensionAlert() {
		showWarning(
				'Parece que seu computador ainda não tem o componente nativo Rest '
				+'Signer instalado. <a id="msg_install_ext_native" '
				+'href="https://github.com/bluecrystalsign/signer-source-vstudio/tree/master/dist">Instale '
					+'o componente daqui</a> '
			);
	}
	
	
	
	function showSucess(msg) {
		showMessage('alert-success', 'Sucesso!', msg);
	}
	function showWarning(msg) {
		showMessage('alert-warning', 'Aviso', msg);
	}	
	function showError(msg) {
		showMessage('alert-danger', 'Erro!', msg);		
	}
	function showMessage(classMsg, caption, msg){
		$("#alert_box").html(
				'<div class="alert '+classMsg+'" >'
				+'<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'
				+'<strong>'+decodeURIComponent(escape(caption))+' </strong> <span > '+decodeURIComponent(escape(msg))+' </span>'
				+'</div>');
		
	}

	function hideMessage(){
		$("#alert_box").html('');
		
	}

	function showSignMessageByStatus(status){
    	if(status == 0){
   		 showSucess(statusMessage[status]);
   	 } else if(json.signStatus == 1) {
   		 showWarning(statusMessage[status]);
   	 } else {
   		 showError(statusMessage[status]);
   	 }

	}

	
// metodos genericos
	function createEnvelope(certificate, payload, hashAlg, time_value, hash_value, sign)
	{
		// STEP 5: CREATE ENV
		$.ajax({
			 type: 'GET',
		        url: 'CreateEnvelope',
		        data: {
		        	hash_value: hash_value,
		        	time_value: time_value,
		        	sa_value: payload,
		        	signed_value: sign,
		        	cert: certificate,
		        	alg: hashAlg
		        },
		        success: function (data) {
		        	var json = $.parseJSON(data);
		        	var signedContent = json.signedContent;
		        	var isOk = json.signStatus == 0? true : false;
		        	var certB64 =  json.certB64;
		        	var certSubject =  json.certSubject;
		        	showSignMessageByStatus(json.signStatus);
//		        	if(json.signStatus == 0){
//		        		 showSucess(statusMessage[json.signStatus]);
//		        	 } else if(json.signStatus == 1) {
//		        		 showWarning(statusMessage[json.signStatus]);
//		        	 } else {
//		        		 showError(statusMessage[json.signStatus]);
//		        	 }
					$("#bt_download").prop('disabled', false);

	        		 $("#signedEnvelope").text(signedContent);
		        },
	            error: function (error) {
	            	showError('error: ' + eval(error));
	            }

			});

		
	}

	function parseCert(certificate){
		$.ajax({
	        type: 'GET',
	        url: 'ParseCert',
	        data: {cert:  certificate},
	        success: function (data) {
	        	var json = $.parseJSON(data);
	        	var html = '<table class="table table-condensed"><tr><th>Nome</th><th>Valor</th></tr>';
	        	 
	        	for(var i = 0; i < json.length; i++){
		        	html += '<tr><td>'+json[i].name+'</td><td>'+json[i].value+'</td></tr>';
	        	 }
	        	 html += '</table>';
	        	 $("#parsedCert").html(html);

	        },
	        error: function (error) {
	        	showError('error: ' + eval(error));
	        }
		});
	}

	// esse metodo tem que suar o provider mas e generico

	function loadSignature(certificate, thumbprint, hashAlg)
	{
		$.ajax({
	        type: 'GET',
	        url: 'LoadSignature',
	        data: { cert: certificate,
	        	alg: hashAlg},
	        success: function (data) {
	           
	            hash_value =  data.hash_value;
	            time_value = data.time_value;
	            sa_value = data.sa_value;
	            
	            // provider
	            var callMethod = provider+"_signContent";
	            window[callMethod](certificate, thumbprint, sa_value,hashAlg, time_value, hash_value);
	            // End Sign
		
		        },
	            error: function (error) {
	            	showError('error: ' + eval(error));
	            }
	     });
		
	}

	
	// REsT SIGNER
	function initBlueCrystal () {

		$('.dropdown-toggle').dropdown();
		$( "#bt_download" ).click(function() {
			download($("#bluc_signed_envelope").text(), 'signature.p7s', "application/pkcs7-mime");
		});
		$("#msg_install_ext_chrome").show();
		$("#extension_alert").show();
		$("#native_alert").show();

		// hide banners page index
		$("#chromeExtension_banner").hide();
		$("#appletCapi_banner").hide();
		$("#appletP11_banner").hide();
		$("#activeX_banner").hide();
		$("#restSigner_banner").hide();
		
		$("#chromeExtension_row").click(function() {
			  showBanner( "chromeExtension" );
		});
		$("#appletCapi_row").click(function() {
			  showBanner( "appletCapi" );
		});
		$("#appletP11_row").click(function() {
			  showBanner( "appletP11" );
		});
		$("#activeX_row").click(function() {
			  showBanner( "activeX" );
		});
		$("#restSigner_row").click(function() {
			  showBanner( "restSigner" );
		});
		try
		{
			restSigner_isActive();
			chromeExtension_isActive();
			activeX_isActive();
			appletCapi_isActive();
			appletP11_isActive();

	        refreshProviers();
		}
		catch(Err)
		{

			showError('Erro (catch): ' + Err);
		}


	}

	function chromeExtension_isActive(){
		mapProviders["chromeExtension"].isActive = false;
	}
	
	function appletCapi_isActive(){
			mapProviders["appletCapi"].isActive = (document.signerCapi != null);
	}

	function appletP11_isActive(){
		mapProviders["appletP11"].isActive = (document.signerCapi != null);
	}


	function activeX_isActive(){
		try
		{
			blucAx = new ActiveXObject("ittru");
			mapProviders["activeX"].isActive = true;
		}
		catch(Err)
		{
			mapProviders["activeX"].isActive = false;
		}	
	}

	
	function restSigner_isActive(){
		var prot = location.protocol;
		 $.ajax({
		        type: 'GET',
		        url: 'http://localhost:8612/test',
		        data: {},
		        success: function (data) {
		        	mapProviders["restSigner"].isActive = true;
		        	refreshProviers();
		        },
		      error: function (error) {
		    	  if(provider=="restSigner")
		    		  showRsExtensionAlert();
					mapProviders["restSigner"].isActive = false;
					refreshProviers();
		      }
		});
		
	}
	function refreshProviers(){
    	for (var m in mapProviders){
    		if(mapProviders[m].isActive){
    			$("#"+m+"_avail").html('<span class="label label-primary">Disponivel</span>');
    			showBanner( m );
    		} else {
    			$("#"+m+"_avail").html('<span class="label label-danger">Indisponivel</span>');
    		}
    	}
	}	
	
	function showBanner(banner){
		$("#chromeExtension_banner").hide();
		$("#appletCapi_banner").hide();
		$("#appletP11_banner").hide();
		$("#activeX_banner").hide();
		$("#restSigner_banner").hide();

		$("#"+banner+"_banner").show();

	}

	var alg = 'sha1';
	var cert_thumb = '';
	var cert_content = '';

	// metodos do provider

	// BEIGN REST SIGNER
// chaamdas - provedor / * neutral	
//	assinar
//		- loadCert
//			* parseCert
//			- getKeySize
//				* loadSigneture
//					-signContent
//						* createEnvelope
	
	// ponto de entrada
	function restSigner_assinar()
	{
		try
			{
   		 	$("#signedEnvelope").text("...");
   		 	$("#parsedCert").text("...");
			$("#bt_download").prop('disabled', true);

			restSigner_loadCert();
			 
	    	// 1 do Cert -->

			}
			catch(Err)
			{

				showError('Erro (catch): ' + Err);
			}
	}
	
	// executa assinatura
	function restSigner_signContent(certificate, thumbprint, payload, hashAlg, time_value, hash_value)
	{
	    $.ajax({
	        type: 'GET',
	        dataType: 'json',
	        url: 'http://localhost:8612/sign',
	        data: { certificate: certificate,
	        	thumbprint: thumbprint,
	        	payload: payload,
	        	hashAlg :hashAlg},
	        success: function (data) {
	           
	        	createEnvelope(certificate, payload, hashAlg, time_value, hash_value, data.sign);
		
		        },
	            error: function (error) {
	            	showError('error: ' + eval(error));
	            }
	     });
		
	}


// interno
	function restSigner_loadCert(){
		$.ajax({
		        type: 'GET',
		        url: 'http://localhost:8612/cert',
		        data: {},
		        success: function (data) {
		        	cert_thumb = data.thumbprint;
		        	cert_content = data.certificate

	        	parseCert(data.certificate);
		        restSigner_getKeySize(data.thumbprint); 	        	
	        	// parseCert -->
	        	
	        	
	        	// Fim do Cert (1) -->
	        	
		        }
		       });
		
	}

	//interno
	function restSigner_getKeySize(thumbprint){
		
	 	// keySize -->
	  	 $.ajax({
	        type: 'GET',
	        url: 'http://localhost:8612/keySize',
	        data: {thumbprint : thumbprint},
	        success: function (data) {
	        	
	            
				if(data.size >= 2048){
					alg = 'sha256';
				}
				// inversao de controle (metodo generico)
				loadSignature(cert_content, cert_thumb, alg);
	        },
	     error: function (error) {
	    	 showError('error: Não foi possivel contactar o restSigner');
	     }
		});

	}


// END REST SIGNER
	
// BEIGN ACTIVE X
	
	// ponto de entrada
	function activeX_assinar()
	{
		try
			{
   		 	$("#signedEnvelope").text("...");
   		 	$("#parsedCert").text("...");
			$("#bt_download").prop('disabled', true);

			blucAx = new ActiveXObject("ittru");

			
			activeX_loadCert();
			 
	    	// 1 do Cert -->

			}
			catch(Err)
			{

				showError('Erro (catch): ' + Err);
			}
	}
	
	// executa assinatura
	function activeX_signContent(certificate, thumbprint, payload, hashAlg, time_value, hash_value)
	{
			//*********
		if(hashAlg = 'sha256'){
			retSign = blucAx.sign(2, sa_value);
		} else {
			retSign = blucAx.sign(0, sa_value);
		}

	        	createEnvelope(certificate, payload, hashAlg, time_value, hash_value, retSign);
		
	}


// interno
	function activeX_loadCert(){
				//************
				cert_content =  blucAx.getCertificate('Certificados', 'Escolha para assinar', '', '');

	        	activeX_getKeySize(); 	        	
	        	// parseCert -->
	        	
	        	parseCert(cert_content);
	        	
	        	// Fim do Cert (1) -->
	        	
		
	}

	//interno
	function activeX_getKeySize(thumbprint){		
//			****
			var keySize = blucAx.getKeySize();
			if(keySize >= 2048){
				alg = 'sha256';
			}

			loadSignature(cert_content, cert_thumb, alg);
	}


// END ACTIVE X

// BEIGN APPLET CAPI
	
	// ponto de entrada
	function appletCapi_assinar()
	{
		try
			{
   		 	$("#signedEnvelope").text("...");
   		 	$("#parsedCert").text("...");
			$("#bt_download").prop('disabled', true);

			appletCapi_loadCert();
			 
	    	// 1 do Cert -->

			}
			catch(Err)
			{

				showError('Erro (catch): ' + Err);
			}
	}
	
	// executa assinatura
	function appletCapi_signContent(certificate, thumbprint, payload, hashAlg, time_value, hash_value)
	{
		
		try {
			//*********
		if(hashAlg = 'sha256'){
			retSign = signer.sign(2, sa_value);
		} else {
			retSign = signer.sign(0, sa_value);
		}
        	createEnvelope(certificate, payload, hashAlg, time_value, hash_value, retSign);
		}catch(Err){
        	showError('error: ' + eval(error));
		}
	}


// interno
	function appletCapi_loadCert(){
				//************
		try {
				cert_content =  signer.getCertificate('Certificados', 'Escolha para assinar', '', '');

				appletCapi_getKeySize(); 	        	
	        	// parseCert -->
	        	
	        	parseCert(cert_content);
	        	
	        	// Fim do Cert (1) -->
	        	
		}catch(Err){
	    	showError('error: ' + eval(error));
		}
		
	}

	//interno
	function appletCapi_getKeySize(thumbprint){		
//			****
		try {
			var keySize = signer.getKeySize();
			if(keySize >= 2048){
				alg = 'sha256';
			}

			loadSignature(cert_content, cert_thumb, alg);
		}catch(Err){
        	showError('error: ' + eval(error));
		}

	}


// END APPLET CAPI
	
	
// BEIGN APPLET PKCS#11
	
	// ponto de entrada
	function appletP11_assinar()
	{
		try
			{
   		 	$("#signedEnvelope").text("...");
   		 	$("#parsedCert").text("...");
			$("#bt_download").prop('disabled', true);

			$("#pinModal").modal('show');
			
			

//			appletP11_loadCert();
			 
	    	// 1 do Cert -->

			}
			catch(Err)
			{

				showError('Erro (catch): ' + Err);
			}
	}

	function appletP11_assinarStep2()
	{
		try
			{

			appletP11_loadCert();
			 
	    	// 1 do Cert -->

			}
			catch(Err)
			{

				showError('Erro (catch): ' + Err);
			}
	}

	
	// executa assinatura
	function appletP11_signContent(certificate, thumbprint, payload, hashAlg, time_value, hash_value)
	{
		
		try {
			//*********
    		var pin_value = $("#pin").val();
    		var cert_alias = $('input[name=cert_alias]:checked').val();
    		var store = $('input[name=store]:checked').val();

    		
			if(hashAlg == 'sha256'){
	        	retSign = document.signer.sign(store, 2, pin_value, cert_alias,
	        			payload);
			} else {
	        	retSign = document.signer.sign(store, 0, pin_value, cert_alias,
	        			payload);
			}
		
			createEnvelope(certificate, payload, hashAlg, time_value, hash_value, retSign);
		}catch(Err){
        	showError('error: ' + eval(error));
		}
	}


// interno
	function appletP11_loadCert(){
				//************
		try {
				var pin_value = $("#pin").val();
				var cert_alias = $('input[name=cert_alias]:checked').val();

				cert_content = document.signer.getCertificate(cert_alias);

				appletP11_getKeySize(cert_alias); 	        	
	        	// parseCert -->
	        	
	        	parseCert(cert_content);
	        	
	        	// Fim do Cert (1) -->
	        	
		}catch(Err){
	    	showError('error: ' + eval(error));
		}
		
	}

	//interno
	function appletP11_getKeySize(cert_alias){		
//			****
		try {
			
			var keySize = document.signer.getKeySize(cert_alias);

			if(keySize >= 2048){
				alg = 'sha256';
			}

			loadSignature(cert_content, cert_thumb, alg);
		}catch(Err){
        	showError('error: ' + eval(error));
		}

	}

	function appletP11_listCerts(){
		var pin_value = $("#pin").val();
		if(pin_value === ''){
			alert('digite o PIN');
			return;
		}

		var store = $('input[name=store]:checked').val();
		if(typeof store === 'undefined'){
			alert('selecione o armazenamento');
			return;
		}

		data = document.signer.listCerts(store,pin_value);
		
		var json = $.parseJSON(data);
		var html = '<div class="radio" style="margin-left: 20px;">';
				
		for(var i = 0; i < json.length; i++){
        	html += "<input type=\"radio\" name=\"cert_alias\" value=\"" + json[i].alias + "\">"
			+ json[i].subject + "<br>";
    	 }
		html += "</div>";
		$("#certChoice").html(html);	
		$("#bt_assinar").prop('disabled', false);
}

// END APPLET PKCS#11
	