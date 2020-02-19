package consumindowebservice;

public class Expense {
	private final String id = "'/Expense'";
	private final String description = "'Nova despesa criada por meio da API do Clarizen'";
	private String dataQueOcorreu;
	private double valor;
	private String codigoMoeda;
	private String externalId;

	public final double getValor() {
		return valor;
	}

	public final void setValor(double valor) {
		this.valor = valor;
	}

	public final String getCodigoMoeda() {
		return codigoMoeda;
	}

	public final void setCodigoMoeda(String codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	public final String getExternalId() {
		return externalId;
	}

	public final void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public final String getId() {
		return id;
	}

	public final String getDescription() {
		return description;
	}

	public String getDataQueOcorreu() {
		return dataQueOcorreu;
	}

	public void setDataQueOcorreu(String dataQueOcorreu) {
		this.dataQueOcorreu = dataQueOcorreu;
	}

}
