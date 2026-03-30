import os
import requests
import time
import json

class MeasurementClient:
    """
    RIPE Atlas Measurement Client for Viseu.
    Handles latency measurements (pings) between edge nodes using the RIPE Atlas API.
    """

    API_URL = "https://atlas.ripe.net/api/v2/measurements/"

    def __init__(self):
        self.api_key = os.getenv("RIPE_ATLAS_API_KEY")
        if not self.api_key:
            env_path = os.path.join(os.path.dirname(__file__), ".env")
            if os.path.exists(env_path):
                with open(env_path, "r") as f:
                    for line in f:
                        if line.startswith("RIPE_ATLAS_API_KEY="):
                            self.api_key = line.split("=")[1].strip()
                            break
        
        if not self.api_key:
            print("WARNING: RIPE_ATLAS_API_KEY for measurement client is not set in environment or .env file.")

    def create_ping_measurement(self, target_ip, label="viseu-latency-check"):
        """
        Creates a new ping measurement to the target IP.
        """
        if not self.api_key:
            return {"error": "API Key missing"}

        payload = {
            "definitions": [
                {
                    "type": "ping",
                    "af": 4,
                    "target": target_ip,
                    "description": label,
                    "is_one_off": True,
                    "use_probes_of_type": "area",
                    "value": "WW" # Worldwide probes
                }
            ],
            "probes": [{"type": "area", "value": "WW", "requested": 1}],
            "is_one_off": True
        }

        headers = {
            "Authorization": f"Key {self.api_key}",
            "Content-Type": "application/json"
        }

        try:
            response = requests.post(self.API_URL, headers=headers, json=payload)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            return {"error": str(e)}

    def get_measurement_result(self, measurement_id):
        """
        Polls for results of a measurement.
        """
        if not self.api_key:
            return {"error": "API Key missing"}

        result_url = f"{self.API_URL}{measurement_id}/results/"
        headers = {"Authorization": f"Key {self.api_key}"}

        try:
            response = requests.get(result_url, headers=headers)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            return {"error": str(e)}

    def wait_for_results(self, m_id, timeout=300, interval=10):
        """
        Synchronously polls the RIPE Atlas API until results are available or timeout is reached.
        """
        elapsed = 0
        while elapsed < timeout:
            results = self.get_measurement_result(m_id)
            
            # RIPE Atlas returns a list of results if available
            if isinstance(results, list) and len(results) > 0:
                return results
            
            # If the response is a dict with an error, it might just be 'Not Found' yet
            if isinstance(results, dict) and "error" in results:
                # We expect 404/waiting if results aren't ready
                pass
            
            time.sleep(interval)
            elapsed += interval
            
        return {"error": f"Polling timed out after {timeout} seconds"}

    def measure_latency(self, target_ip):
        """
        High-level function to measure latency, wait for results, and return average RTT.
        """
        create_res = self.create_ping_measurement(target_ip)
        if "error" in create_res:
            return create_res

        m_id = create_res.get("measurements", [None])[0]
        if not m_id:
            return {"error": f"Failed to retrieve measurement ID. Response: {create_res}"}

        # Wait for results synchronously
        results = self.wait_for_results(m_id)
        
        if isinstance(results, dict) and "error" in results:
            return results

        # Parse RIPE Atlas ping result format
        try:
            # Taking the first result from the probe
            first_result = results[0]
            avg_rtt = first_result.get("avg", -1.0)
            
            if avg_rtt == -1.0:
                # Fallback to 'min' or 'max' if 'avg' is missing (unlikely for ping)
                avg_rtt = first_result.get("min", -1.0)

            return {
                "status": "success",
                "target_ip": target_ip,
                "latency_ms": avg_rtt,
                "measurement_id": m_id
            }
        except (IndexError, KeyError, TypeError) as e:
            return {"error": f"Failed to parse measurement results: {str(e)}", "raw_result": results}

if __name__ == "__main__":
    client = MeasurementClient()
    print(f"Viseu Measurement Client Initialized. API Key: {'Detected' if client.api_key else 'Missing'}")
