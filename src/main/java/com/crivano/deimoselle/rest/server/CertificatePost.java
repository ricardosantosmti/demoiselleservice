package com.crivano.deimoselle.rest.server;

import java.util.Map;

import org.demoiselle.signer.core.extension.BasicCertificate;
import org.demoiselle.signer.core.extension.CertificateExtra;

import com.crivano.blucservice.api.IBlueCrystal.CertDetails;
import com.crivano.blucservice.api.IBlueCrystal.CertificatePostRequest;
import com.crivano.blucservice.api.IBlueCrystal.CertificatePostResponse;
import com.crivano.blucservice.api.IBlueCrystal.ICertificatePost;
import com.crivano.swaggerservlet.ISwaggerCacheableMethod;
import com.crivano.swaggerservlet.Swagger;

public class CertificatePost implements ICertificatePost, ISwaggerCacheableMethod {

	@Override
	public String getContext() {
		return "deimoselle-certificate";
	}

	@Override
	public void run(CertificatePostRequest req, CertificatePostResponse resp) throws Exception {
		BasicCertificate cb = new BasicCertificate(req.certificate);
		CertificateExtra ce = new CertificateExtra(cb.getX509Certificate());

		resp.subject = cb.getCertificateSubjectDN().toString();
		resp.cn = cb.getCertificateSubjectDN().getProperty("CN");
		resp.name = cb.getName();
		resp.cpf = ce.getOID_2_16_76_1_3_1().getCPF();

		resp.certdetails = new CertDetails();
		resp.certdetails.cpf0 = ce.getOID_2_16_76_1_3_1().getCPF();
		if (ce.isCertificatePF())
			resp.certdetails.birth_date0 = ce.getOID_2_16_76_1_3_1().getCPF();
		if (ce.isCertificatePJ())
			resp.certdetails.cnpj0 = ce.getOID_2_16_76_1_3_3().getCNPJ();
		resp.certdetails.san_email0 = ce.getEmail();
	}

	public static void fillCertificateDetails(CertDetails certdetails, Map<String, String> map) throws Exception {
		for (String key : map.keySet()) {
			Swagger.set(certdetails, key, map.get(key));
		}
	}
}
