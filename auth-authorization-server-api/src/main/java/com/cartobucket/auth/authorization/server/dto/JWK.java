package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "JSON Web Key")
@JsonbNillable(false)
public class JWK {
    
    @JsonbProperty("kty")
    @Schema(description = "Key type", required = true, example = "RSA")
    private String kty;
    
    @JsonbProperty("use")
    @Schema(description = "Public key use", example = "sig")
    private String use;
    
    @JsonbProperty("key_ops")
    @Schema(description = "Key operations")
    private List<String> keyOps;
    
    @JsonbProperty("alg")
    @Schema(description = "Algorithm", example = "RS256")
    private String alg;
    
    @JsonbProperty("kid")
    @Schema(description = "Key ID")
    private String kid;
    
    @JsonbProperty("x5c")
    @Schema(description = "X.509 Certificate Chain")
    private List<String> x5c;
    
    @JsonbProperty("x5t")
    @Schema(description = "X.509 Certificate SHA-1 Thumbprint")
    private String x5t;
    
    @JsonbProperty("x5t#S256")
    @Schema(description = "X.509 Certificate SHA-256 Thumbprint")
    private String x5tS256;
    
    @JsonbProperty("x5u")
    @Schema(description = "X.509 URL")
    private String x5u;
    
    @JsonbProperty("n")
    @Schema(description = "RSA modulus")
    private String n;
    
    @JsonbProperty("e")
    @Schema(description = "RSA exponent")
    private String e;
    
    @JsonbProperty("d")
    @Schema(description = "RSA private exponent")
    private String d;
    
    @JsonbProperty("p")
    @Schema(description = "RSA first prime factor")
    private String p;
    
    @JsonbProperty("q")
    @Schema(description = "RSA second prime factor")
    private String q;
    
    @JsonbProperty("dp")
    @Schema(description = "RSA first factor CRT exponent")
    private String dp;
    
    @JsonbProperty("dq")
    @Schema(description = "RSA second factor CRT exponent")
    private String dq;
    
    @JsonbProperty("qi")
    @Schema(description = "RSA first CRT coefficient")
    private String qi;
    
    public String getKty() {
        return kty;
    }
    
    public void setKty(String kty) {
        this.kty = kty;
    }
    
    public String getUse() {
        return use;
    }
    
    public void setUse(String use) {
        this.use = use;
    }
    
    public List<String> getKeyOps() {
        return keyOps;
    }
    
    public void setKeyOps(List<String> keyOps) {
        this.keyOps = keyOps;
    }
    
    public String getAlg() {
        return alg;
    }
    
    public void setAlg(String alg) {
        this.alg = alg;
    }
    
    public String getKid() {
        return kid;
    }
    
    public void setKid(String kid) {
        this.kid = kid;
    }
    
    public List<String> getX5c() {
        return x5c;
    }
    
    public void setX5c(List<String> x5c) {
        this.x5c = x5c;
    }
    
    public String getX5t() {
        return x5t;
    }
    
    public void setX5t(String x5t) {
        this.x5t = x5t;
    }
    
    public String getX5tS256() {
        return x5tS256;
    }
    
    public void setX5tS256(String x5tS256) {
        this.x5tS256 = x5tS256;
    }
    
    public String getX5u() {
        return x5u;
    }
    
    public void setX5u(String x5u) {
        this.x5u = x5u;
    }
    
    public String getN() {
        return n;
    }
    
    public void setN(String n) {
        this.n = n;
    }
    
    public String getE() {
        return e;
    }
    
    public void setE(String e) {
        this.e = e;
    }
    
    public String getD() {
        return d;
    }
    
    public void setD(String d) {
        this.d = d;
    }
    
    public String getP() {
        return p;
    }
    
    public void setP(String p) {
        this.p = p;
    }
    
    public String getQ() {
        return q;
    }
    
    public void setQ(String q) {
        this.q = q;
    }
    
    public String getDp() {
        return dp;
    }
    
    public void setDp(String dp) {
        this.dp = dp;
    }
    
    public String getDq() {
        return dq;
    }
    
    public void setDq(String dq) {
        this.dq = dq;
    }
    
    public String getQi() {
        return qi;
    }
    
    public void setQi(String qi) {
        this.qi = qi;
    }
}