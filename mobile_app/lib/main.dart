import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

// Modello di attività
class Activity {
  final String titolo;
  final String categoria;
  final String image;

  Activity({required this.titolo, required this.categoria, required this.image});

  factory Activity.fromJson(Map<String, dynamic> json) {
    return Activity(
      titolo: json['titolo'],
      categoria: json['categoria'],
      image: json['image'],
    );
  }
}

// Funzione per caricare le attività
Future<List<Activity>> fetchActivities(String keyword) async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/attivita?keyword=$keyword'));
  if (response.statusCode == 200) {
    Iterable list = json.decode(response.body);
    return list.map((json) => Activity.fromJson(json)).toList();
  } else {
    throw Exception('Errore nel caricamento delle attività');
  }
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Gestione Attività',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MobileHomePage(),
    );
  }
}

class MobileHomePage extends StatefulWidget {
  const MobileHomePage({super.key});
  
  @override
  State<MobileHomePage> createState() => _MobileHomePageState();
}

class _MobileHomePageState extends State<MobileHomePage> {
  List<Activity> activities = [];
  String keyword = '';
  
  @override
  void initState() {
    super.initState();
    _loadActivities();
  }
  
  Future<void> _loadActivities() async {
    try {
      final data = await fetchActivities(keyword);
      setState(() {
        activities = data;
      });
    } catch (e) {
      // Gestisci l'errore, ad esempio mostrando un messaggio a schermo
      print(e);
    }
  }
  
  void _showCreateActivityDialog() {
    showDialog(
      context: context,
      builder: (context) {
        return CreateActivityDialog();
      },
    );
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Gestione Attività"),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            // Header con immagine e titolo
            Stack(
              children: [
                Image.asset(
                  'assets/images/desk.jpg',
                  height: 160,
                  width: double.infinity,
                  fit: BoxFit.cover,
                ),
                Container(
                  height: 160,
                  width: double.infinity,
                  color: Colors.black.withOpacity(0.3),
                ),
                const Positioned(
                  bottom: 16,
                  left: 16,
                  child: Text(
                    "Gestione Attività",
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  // Barra di ricerca
                  TextField(
                    decoration: InputDecoration(
                      hintText: "Cerca attività...",
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                    onChanged: (value) {
                      setState(() {
                        keyword = value;
                      });
                      _loadActivities();
                    },
                  ),
                  const SizedBox(height: 16),
                  // Bottoni per creare attività
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      ElevatedButton(
                        onPressed: _showCreateActivityDialog,
                        style: ElevatedButton.styleFrom(backgroundColor: Colors.grey),
                        child: const Text("Crea Nuova Attività"),
                      ),
                      ElevatedButton(
                        onPressed: _showCreateActivityDialog,
                        style: ElevatedButton.styleFrom(backgroundColor: Colors.blue),
                        child: const Text("Crea Nuova Attività"),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  // Griglia per visualizzare le attività
                  GridView.builder(
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      crossAxisSpacing: 8,
                      mainAxisSpacing: 8,
                    ),
                    itemCount: activities.length,
                    itemBuilder: (context, index) {
                      final act = activities[index];
                      return Card(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Image.network(
                              'http://10.0.2.2:8080/images/${act.image}',
                              height: 80,
                              width: double.infinity,
                              fit: BoxFit.cover,
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Text(
                                act.titolo,
                                style: const TextStyle(fontWeight: FontWeight.bold),
                              ),
                            ),
                            Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 8.0),
                              child: Text(
                                act.categoria,
                                style: const TextStyle(color: Colors.grey),
                              ),
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _showCreateActivityDialog,
        tooltip: 'Crea Nuova Attività',
        child: const Icon(Icons.add),
      ),
    );
  }
}

class CreateActivityDialog extends StatefulWidget {
  @override
  State<CreateActivityDialog> createState() => _CreateActivityDialogState();
}

class _CreateActivityDialogState extends State<CreateActivityDialog> {
  final _formKey = GlobalKey<FormState>();
  String titolo = '';
  String descrizione = '';
  String luogo = '';
  int maxPartecipanti = 0;
  DateTime? dataInizio;
  DateTime? dataFine;
  String categoria = '';
  // Per gestire l'immagine, in un'app reale puoi usare un package come image_picker

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text("Crea Nuova Attività"),
      content: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                decoration: const InputDecoration(labelText: "Titolo"),
                onSaved: (value) => titolo = value ?? '',
                validator: (value) => value == null || value.isEmpty ? "Inserisci il titolo" : null,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: "Descrizione"),
                onSaved: (value) => descrizione = value ?? '',
                validator: (value) => value == null || value.isEmpty ? "Inserisci la descrizione" : null,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: "Luogo"),
                onSaved: (value) => luogo = value ?? '',
                validator: (value) => value == null || value.isEmpty ? "Inserisci il luogo" : null,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: "Numero massimo di partecipanti"),
                keyboardType: TextInputType.number,
                onSaved: (value) => maxPartecipanti = int.tryParse(value ?? '') ?? 0,
                validator: (value) => value == null || value.isEmpty ? "Inserisci un numero" : null,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: "Categoria"),
                onSaved: (value) => categoria = value ?? '',
                validator: (value) => value == null || value.isEmpty ? "Inserisci la categoria" : null,
              ),
              // Per dataInizio e dataFine, per semplicità usiamo TextField che si converte in DateTime
              TextFormField(
                decoration: const InputDecoration(labelText: "Data Inizio (YYYY-MM-DD HH:MM)"),
                onSaved: (value) {
                  if (value != null && value.isNotEmpty) {
                    dataInizio = DateTime.tryParse(value);
                  }
                },
                validator: (value) => value == null || value.isEmpty ? "Inserisci la data di inizio" : null,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: "Data Fine (YYYY-MM-DD HH:MM)"),
                onSaved: (value) {
                  if (value != null && value.isNotEmpty) {
                    dataFine = DateTime.tryParse(value);
                  }
                },
                validator: (value) => value == null || value.isEmpty ? "Inserisci la data di fine" : null,
              ),
              const SizedBox(height: 8),
              // Placeholder per l'immagine
              ElevatedButton(
                onPressed: () {
                  // Qui potresti integrare image_picker per selezionare l'immagine
                },
                child: const Text("Seleziona Immagine"),
              ),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () {
            Navigator.pop(context);
          },
          child: const Text("Annulla"),
        ),
        ElevatedButton(
          onPressed: () {
            if (_formKey.currentState!.validate()) {
              _formKey.currentState!.save();
              // Gestisci l'invio dei dati al backend o la logica per creare l'attività
              Navigator.pop(context);
            }
          },
          child: const Text("Crea"),
        ),
      ],
    );
  }
}
