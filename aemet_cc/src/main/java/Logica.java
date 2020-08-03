
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * 
 * @author javier
 *
 */
public class Logica {

	/**
	 * Atributos
	 */
	private String idEstacion = "";
	private final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZpZXJhcmd1ZXNvc290b0BnbWFpbC5jb20iLCJqdGkiOiI0MDYyMmM4ZS1iNjI2LTRhNjctYmNiYi0yYTdiMTU4Yjc4OWIiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTU5NTc5Mzc3OCwidXNlcklkIjoiNDA2MjJjOGUtYjYyNi00YTY3LWJjYmItMmE3YjE1OGI3ODliIiwicm9sZSI6IiJ9.F1lkLkD2pPP1lM57ywbRHrqvisepWGKoPIOXAw6gLKc";

	/**
	 * Constructor por defecto
	 */
	public Logica() {
		idEstacion = "4452";
	}

	/**
	 * Constructor parametrizado
	 * 
	 * @param idEstacion
	 */
	public Logica(String idEstacion) {
		this.idEstacion = idEstacion;
	}

	/**
	 * Metodo de obtencion de datos diarios en funcion de la estacion introducida
	 */
	public void getDatosDia() {
		try {
			String response = Unirest
					.get("https://opendata.aemet.es/opendata/api/observacion/convencional/datos/estacion/{idema}")
					.routeParam("idema", idEstacion).queryString("api_key", API_KEY).header("cache-control", "no-cache")
					.asJson().getBody().getObject().getString("datos");

			JSONArray vector = Unirest.get(response).asJson().getBody().getArray();

			int lluvias = 0;
			for (Object jso : vector) {
				JSONObject j = (JSONObject) jso;
				lluvias = j.getInt("prec");
			}
			System.out.println("Hoy ha llovido: " + lluvias);

		} catch (UnirestException e) {
			System.out.println(e);
		}
	}

	/**
	 * Metodo de obtencion de datos normales recogidos por la estacion seleccionada
	 */
	public void getDatosNormales() {
		try {
			String response = Unirest
					.get("https://opendata.aemet.es/opendata/api/valores/climatologicos/normales/estacion/{idema}")
					.routeParam("idema", idEstacion).queryString("api_key", API_KEY).header("cache-control", "no-cache")
					.asJson().getBody().getObject().getString("datos");
			
			JSONArray vector = Unirest.get(response).asJson().getBody().getArray();
			JSONObject anio = vector.getJSONObject(vector.length()-1);
			
			System.out.println("Valor maximo de precipitaciones por año: " + anio.get("p_mes_max"));
			
		} catch (UnirestException e) {
			System.out.println(e);
		}
	}

}
