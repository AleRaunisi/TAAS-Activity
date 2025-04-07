from Crypto.Cipher import AES, DES
from Crypto.Util.Padding import unpad
from base64 import b64decode

# Inserisci i tuoi dati
ciphertext = b64decode("ac2d5c1e794dc8f7|03a771022b4a938074eb471ba1f3349508d0c8d255e89ebe365651565f8cc263ef958adbd6cd1f0133159204f7eb9b2b3920e7e42fb1005d84ee2581ec628f64b957019df6f587be11bd0bbfe7bee9")  # Messaggio cifrato
key = b'QWEasd123?'  # Deve essere della lunghezza giusta per ogni algoritmo (vedi sotto)
iv = b'\x00' * 16  # IV predefinito (se non conosci l'IV, prova con zero)

# Funzione per testare AES e DES
def test_algorithms(ciphertext, key):
    try:
        # AES ECB
        aes_ecb = AES.new(key, AES.MODE_ECB)
        print("AES ECB:", unpad(aes_ecb.decrypt(ciphertext), AES.block_size).decode())
    except Exception:
        print("AES ECB fallito")

    try:
        # AES CBC
        aes_cbc = AES.new(key, AES.MODE_CBC, iv)
        print("AES CBC:", unpad(aes_cbc.decrypt(ciphertext), AES.block_size).decode())
    except Exception:
        print("AES CBC fallito")

    try:
        # DES ECB
        des_ecb = DES.new(key[:8], DES.MODE_ECB)  # DES usa chiavi da 8 byte
        print("DES ECB:", unpad(des_ecb.decrypt(ciphertext), DES.block_size).decode())
    except Exception:
        print("DES ECB fallito")

    try:
        # DES CBC
        des_cbc = DES.new(key[:8], DES.MODE_CBC, iv[:8])  # DES usa IV da 8 byte
        print("DES CBC:", unpad(des_cbc.decrypt(ciphertext), DES.block_size).decode())
    except Exception:
        print("DES CBC fallito")

# Testa gli algoritmi
test_algorithms(ciphertext, key)