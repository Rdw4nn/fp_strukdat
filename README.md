# FINAL PROJECT STRUKDAT: Sistem Transportasi Umum Surabaya
## Opsi 5 - Public Transport Planner 

## Kelompok 1
| No  | Nama                           | NRP        |
| --- | ------------------------------ | ---------- |
| 1   | Revalinda Bunga Nayla Laksono  | 5027251011 |
| 2   | Putri Permata Sabila           | 5027251047 |
| 3   | Nathania Tiara Wahyudi         | 5027251089 |
| 4   | Jude Athala Yazid Sari         | 5027251098 |
| 5   | Muhammmad Ridwan               | 5027251113 |

---

## 1. Deskripsi Masalah

Proyek ini merupakan simulasi sistem informasi transportasi umum kota Surabaya yang dibangun menggunakan struktur data dan algoritma dasar. Program memodelkan jaringan halte bus dan stasiun kereta sebagai graf berarah berbobot, kemudian menyediakan beberapa fitur pencarian rute yang dapat digunakan oleh pengguna.

---

## 2. Dataset

Dataset terdiri dari dua file CSV yang berada di direktori `data/`.

### nodes.csv

Berisi 30 node (halte dan stasiun). Format kolom: `id, nama, jenis, area, fasilitas, status`

| Jenis | Jumlah | Prefix ID |
|---|---|---|
| Kereta | 10 | S01 s.d. S10 |
| Bus | 20 | B01 s.d. B20 |

Satu node berstatus `Nonaktif`: B16 (Halte Sidosermo). Node ini tidak akan memiliki edge masuk karena `Graph.addEdge` memvalidasi status node sebelum menambahkan edge.

### edges.csv

Berisi 65 edge berarah. Format kolom: `id_edge, dari, ke, waktu_menit, biaya_rupiah, jenis_transportasi`

Rentang bobot:
- Waktu: 5 menit s.d. 25 menit
- Biaya: Rp 2.000 s.d. Rp 8.000

Hampir semua edge berpasangan bolak-balik (dua edge terpisah untuk dua arah). Edge yang melibatkan node nonaktif tidak berhasil ditambahkan ke graf saat loading.

---

## 3. Struktur Graph yang Digunakan

### Graph (Adjacency List)

Graf direpresentasikan menggunakan adjacency list dengan `HashMap<String, List<Edge>>`. Setiap key adalah ID node, dan value-nya adalah list edge yang keluar dari node tersebut.

Contoh representasi sebagian graf dari dataset:

```text
S01 (Stasiun Surabaya Gubeng)
├── E01 -> S03 | 8 menit  | Rp 3.000 | Kereta
├── E11 -> S02 | 15 menit | Rp 5.000 | Kereta
├── E52 -> B20 | 10 menit | Rp 4.000 | Bus
└── E64 -> B06 | 12 menit | Rp 5.000 | Bus

S03 (Stasiun Wonokromo)
├── E02 -> S01 | 8 menit  | Rp 3.000 | Kereta
├── E03 -> S04 | 12 menit | Rp 4.000 | Kereta
├── E15 -> S05 | 10 menit | Rp 3.000 | Kereta
└── E22 -> B01 | 10 menit | Rp 5.000 | Bus

B06 (Halte Tunjungan)
├── E34 -> B07 | 8 menit  | Rp 3.000 | Bus
└── E36 -> B05 | 5 menit  | Rp 3.000 | Bus
```

Graf bersifat directed (berarah) sehingga edge dari A ke B tidak otomatis berlaku sebaliknya. Dataset ini menyediakan edge bolak-balik secara eksplisit, misalnya E01 (S01->S03) dan E02 (S03->S01) adalah pasangan arah yang berlawanan.

Implementasi: `model/Graph.java`

---

## 4. Struktur Tree yang Digunakan

### Trie

*Prefix tree* (Trie) digunakan untuk fitur pencarian halte berdasarkan awalan nama. Setiap node dalam Trie menyimpan satu karakter, dan setiap *path* dari *root* ke node yang ditandai dengan `isEndOfWord = true` merepresentasikan satu nama halte secara utuh. Node akhir tersebut menyimpan referensi ke objek `Node` (model halte) yang bersangkutan.

Visualisasi Trie setelah penambahan tiga nama: "stasiun surabaya gubeng" (S01), "stasiun surabaya pasar turi" (S02), dan "halte diponegoro" (B08). Seluruh nama dikonversi menjadi huruf kecil (*lowercase*) sebelum dimasukkan.

```text
root
├── s - t - a - s - i - u - n - (spasi) - s - u - r - a - b - a - y - a - (spasi)
│                                                                           ├── g - u - b - e - n - g  [END -> S01]
│                                                                           └── p - a - s - a - r - (spasi) - t - u - r - i  [END -> S02]
└── h - a - l - t - e - (spasi) - d - i - p - o - n - e - g - o - r - o  [END -> B08]
```

Ketika user mengetik prefix `"sta"`, traversal dimulai dari root melalui `s -> t -> a`, kemudian semua node di bawah subtree tersebut dikumpulkan secara rekursif dan dikembalikan sebagai hasil pencarian.

Ketika prefix tidak ditemukan, misalnya `"xyz"`, traversal berhenti di `root -> x -> null` dan fungsi langsung mengembalikan list kosong.

Implementasi: `model/Trie.java`

---

## 5. Struktur MinHeap yang Digunakan
### MinHeap

MinHeap digunakan oleh algoritma Dijkstra sebagai priority queue untuk selalu memproses node dengan bobot terkecil terlebih dahulu. Heap diimplementasikan menggunakan array dengan properti:
- Parent dari node di index `i` ada di index `(i-1)/2`
- Left child ada di index `2*i + 1`
- Right child ada di index `2*i + 2`

Visualisasi MinHeap setelah insert tiga elemen dengan nilai 20, 5, 12:

```text
Insert 20:
Array: [20]

Insert 5:
Array sementara: [20, 5]
Up-heapify: parent(index 1) = index 0, nilai 5 < 20, swap
Array: [5, 20]

Insert 12:
Array sementara: [5, 20, 12]
Up-heapify: parent(index 2) = index 0, nilai 12 > 5, tidak swap
Array: [5, 20, 12]

Representasi pohon:
      5
     / \
   20   12
```

ExtractMin mengambil elemen di index 0 (nilai 5), memindahkan elemen terakhir ke posisi root, lalu menjalankan down-heapify:

```text
Setelah extract 5:
Pindah elemen terakhir (12) ke root
Array sementara: [12, 20]
Down-heapify: left child (index 1) = 20, nilai 12 < 20, tidak swap
Array: [12, 20]

Representasi pohon:
      12
     /
   20

Urutan extract: 5, 12, 20
```

Implementasi: `model/MinHeap.java`

---

## 6. Algoritma yang Digunakan

### BFS (Breadth-First Search)

BFS digunakan untuk mencari rute dengan jumlah perpindahan halte (transit) paling sedikit. BFS menjamin jalur terpendek dalam satuan langkah karena menjelajah level demi level dari node asal.

Cara kerja:
1. Masukkan path awal `[asalId]` ke dalam queue.
2. Ambil path terdepan dari queue.
3. Untuk setiap tetangga dari node terakhir di path tersebut:
   - Jika tetangga adalah tujuan: kembalikan path ini sebagai hasil.
   - Jika tetangga belum dikunjungi: buat path baru dengan tetangga ditambahkan, masukkan ke queue.
4. Ulangi sampai queue kosong (tidak ada rute) atau tujuan ditemukan.

Catatan implementasi:
- Edge dengan `aktif = false` di-skip, digunakan oleh fitur RouteSimulator.
- Node dengan status `Nonaktif` di dataset juga di-skip.
- Setelah path ditemukan, total waktu dan biaya dihitung terpisah melalui `hitungTotalWaktuBiaya()`.

Implementasi: `algorithm/BFS.java`

---

### Dijkstra

Dijkstra digunakan untuk mencari rute tercepat (berdasarkan waktu) atau termurah (berdasarkan biaya). Algoritma ini menggunakan `PriorityQueue` bawaan Java dan memilih node dengan bobot akumulasi terkecil di setiap langkah.

Cara kerja:
1. Inisialisasi semua jarak ke `Integer.MAX_VALUE`, jarak asal ke 0.
2. Masukkan node asal ke priority queue.
3. Ambil node dengan jarak terkecil dari queue.
4. Untuk setiap edge keluar dari node tersebut, hitung jarak baru. Jika lebih kecil dari jarak yang tersimpan, perbarui dan masukkan ke queue.
5. Ulangi sampai node tujuan diproses atau queue kosong.

Mode yang tersedia:
- `"waktu"` menggunakan `getWaktuMenit()` sebagai bobot.
- `"biaya"` menggunakan `getBiayaRupiah()` sebagai bobot.

Implementasi: `algorithm/Djikstra.java`

---

## 7. Design Decision Log

1. **Penggunaan HashMap pada Graph:** Daripada menggunakan *Adjacency Matrix* (array 2D) yang memakan memori **O(V²)**, implementasi *Adjacency List* berbasis `HashMap<String, List<Edge>>` jauh lebih optimal untuk jaringan rute transportasi kota yang umumnya *sparse* (lengang), memangkas pemakaian memori menjadi **O(V + E)**.
2. **Penyimpanan Path BFS vs Dijkstra:** Pada algoritma BFS, seluruh *path* disimpan berulang di dalam memori antrean (`Queue<List<String>>`). Meskipun memakan ruang lebih, ini dipertahankan karena BFS berfokus pada pencarian *hop* terpendek tanpa peduli bobot. Sebagai optimasi di Dijkstra, memori dihemat dengan hanya menyimpan *pointer* di `Map<String, String> sebelum` dan melakukan *backtracking* (rekonstruksi rute) saat node tujuan sudah ditemukan.
3. **Validasi Status Node Sejak Awal:** Keputusan untuk menolak pembuatan *edge* menuju halte berstatus "Nonaktif" diletakkan pada tahap *loading* data (di dalam `Graph.addEdge()`). Hal ini sangat menyederhanakan logika pencarian, karena algoritma BFS/Dijkstra tidak perlu lagi melakukan pengecekan status *node* berulang-ulang di setiap iterasi.
4. **Trie untuk Pencarian Fleksibel:** Penggunaan Regex atau fungsi bawaan `String.contains()` pada Array akan memakan waktu **O(N × L)** setiap kali pencarian dilakukan. Pembuatan struktur data *Trie* memangkas waktu pencarian secara drastis menjadi **O(L + R)** (di mana *L* = panjang karakter input, *R* = jumlah kemunculan), membuat program jauh lebih responsif.

---

## 8. Tracing Manual Algoritma

### Tracing Trie: Insert dan Search

**Data yang diinsert:** "stasiun surabaya gubeng" (S01), "stasiun surabaya pasar turi" (S02), "halte diponegoro" (B08)

Semua nama diubah ke lowercase sebelum diproses.

**Proses insert "stasiun surabaya gubeng":**

Setiap karakter dibuat node baru jika belum ada di `children`. Setelah traversal selesai di karakter terakhir `g`, node tersebut di-set `isEndOfWord = true` dan referensi ke objek Node S01 ditambahkan ke `associatedNodes`.

**Search dengan prefix "sta":**

Traversal: `root -> s -> t -> a`

Node `a` berhasil ditemukan. Dari sini, `findAllNodes` dijalankan secara rekursif ke seluruh subtree di bawahnya. Semua node yang memiliki `isEndOfWord = true` dikumpulkan dan dikembalikan. Hasil: S01 dan S02.

**Search dengan prefix "xyz":**

Traversal: `root -> x` menghasilkan `null` karena `x` tidak ada di children root. Fungsi langsung mengembalikan list kosong tanpa traversal lebih lanjut.

---

### Tracing MinHeap: Insert dan ExtractMin

**Skenario:** insert node Gubeng (nilai 20), Wonokromo (nilai 5), Gedangan (nilai 12)

**Step 1: Insert Gubeng (20)**
- Array: `[Gubeng:20]`
- current = 0, tidak ada parent, selesai.

**Step 2: Insert Wonokromo (5)**
- Array sementara: `[Gubeng:20, Wonokromo:5]`
- current = 1, parent(1) = 0
- heap[1].value (5) < heap[0].value (20) -> swap
- Array: `[Wonokromo:5, Gubeng:20]`

**Step 3: Insert Gedangan (12)**
- Array sementara: `[Wonokromo:5, Gubeng:20, Gedangan:12]`
- current = 2, parent(2) = 0
- heap[2].value (12) > heap[0].value (5) -> tidak swap
- Array: `[Wonokromo:5, Gubeng:20, Gedangan:12]`

**ExtractMin #1:**
- min = Wonokromo:5
- Pindah elemen terakhir ke root: `[Gedangan:12, Gubeng:20]`
- minHeapify(0):
  - left(0) = index 1 -> Gubeng:20
  - right(0) = index 2 -> tidak ada
  - smallest = 0 (nilai 12 < 20), tidak swap
- Hasil: Wonokromo (5)

**ExtractMin #2:**
- min = Gedangan:12
- Pindah elemen terakhir ke root: `[Gubeng:20]`
- minHeapify(0): tidak ada child
- Hasil: Gedangan (12)

**ExtractMin #3:**
- min = Gubeng:20
- Array: `[]`
- Hasil: Gubeng (20)

**Urutan output:** Wonokromo (5) -> Gedangan (12) -> Gubeng (20)

---

### Tracing BFS: Minimum Transit

**Skenario A: Ada edge langsung**

Asal: S01 (Stasiun Surabaya Gubeng), Tujuan: B06 (Halte Tunjungan)
Edge keluar dari S01: E01->S03, E11->S02, E52->B20, E64->B06

Inisialisasi:
- queue  : `[[S01]]`
- visited: `{S01}`

Iterasi 1:
- Poll: `[S01]`
- Periksa tetangga S01:
  - S03: belum dikunjungi, masuk queue
  - S02: belum dikunjungi, masuk queue
  - B20: belum dikunjungi, masuk queue
  - B06: B06 == tujuan -> DITEMUKAN

Path final  : `[S01, B06]`
- jumlahTransit: 1
- totalWaktu  : 12 menit (edge E64)
- totalBiaya  : Rp 5.000 (edge E64)

BFS selesai di iterasi pertama karena ada edge langsung dari S01 ke B06.

**Skenario B: Tidak ada edge langsung**

Asal: B13 (Halte ITS Sukolilo), Tujuan: B07 (Halte Urip Sumoharjo)
Edge keluar dari B13: E45->B14, E56->B18, E62->S01

Inisialisasi:
- queue  : `[[B13]]`
- visited: `{B13}`

Iterasi 1:
- Poll: `[B13]`
- Tetangga B13:
  - B14 -> queue: `[[B13,B14]]`, visited: `{B13,B14}`
  - B18 -> queue: `[[B13,B14],[B13,B18]]`, visited: `{...,B18}`
  - S01 -> queue: `[[B13,B14],[B13,B18],[B13,S01]]`, visited: `{...,S01}`

Iterasi 2:
- Poll: `[B13,B14]`
- Tetangga B14: B13(visited), B19
  - B19 -> queue: `[...,[B13,B14,B19]]`, visited: `{...,B19}`

Iterasi 3:
- Poll: `[B13,B18]`
- Tetangga B18: B17, B13(visited)
  - B17 -> queue: `[...,[B13,B18,B17]]`, visited: `{...,B17}`

Iterasi 4:
- Poll: `[B13,S01]`
- Tetangga S01: S03, S02, B20, B06, B13(visited)
  - S03 -> queue tambah `[B13,S01,S03]`
  - S02 -> queue tambah `[B13,S01,S02]`
  - B20 -> queue tambah `[B13,S01,B20]`
  - B06 -> queue tambah `[B13,S01,B06]`

Iterasi 5:
- Poll: `[B13,B14,B19]`
- Tetangga B19: B14(visited), B20(visited)
- Tidak ada node baru.

Iterasi 6:
- Poll: `[B13,B18,B17]`
- Tetangga B17: B18(visited), B11
  - B11 -> queue tambah `[B13,B18,B17,B11]`

... iterasi berlanjut ...

Iterasi N:
- Poll: `[B13,S01,B06]`
- Tetangga B06: B07, B05
  - B07 == tujuan -> DITEMUKAN

Path final   : `[B13, S01, B06, B07]`
- jumlahTransit: 3
- totalWaktu   : 18 + 12 + 8 = 38 menit
- totalBiaya   : Rp 6.000 + Rp 5.000 + Rp 3.000 = Rp 14.000

BFS menjamin ini adalah path dengan transit paling sedikit yang bisa ditemukan dari B13 ke B07.

---

### Tracing Dijkstra: Rute Tercepat

**Skenario:** S03 (Stasiun Wonokromo) -> B06 (Halte Tunjungan), mode waktu

Edge relevan dari dataset:
- S03 -> S01: 8 menit (E02)
- S03 -> S04: 12 menit (E03)
- S03 -> S05: 10 menit (E15)
- S03 -> B01: 10 menit (E22)
- S01 -> S02: 15 menit (E11)
- S01 -> B20: 10 menit (E52)
- S01 -> B06: 12 menit (E64)

Inisialisasi:
- jarak[semua] = MAX_VALUE
- jarak[S03]   = 0
- PQ     : `[(S03, 0)]`
- visited: `{}`

Step 1: Poll S03 (jarak=0)
- visited: `{S03}`
- Update tetangga:
  - S01: 0+8  = 8,  sebelum[S01] = S03
  - S04: 0+12 = 12, sebelum[S04] = S03
  - S05: 0+10 = 10, sebelum[S05] = S03
  - B01: 0+10 = 10, sebelum[B01] = S03
- PQ: `[(S01,8), (S05,10), (B01,10), (S04,12)]`

Step 2: Poll S01 (jarak=8)
- visited: `{S03, S01}`
- Update tetangga:
  - S03: 8+8=16  > jarak[S03]=0, skip
  - S02: 8+15=23, sebelum[S02] = S01
  - B20: 8+10=18, sebelum[B20] = S01
  - B06: 8+12=20, sebelum[B06] = S01
  - B13: 8+18=26, sebelum[B13] = S01
- PQ: `[(S05,10),(B01,10),(S04,12),(B20,18),(B06,20),(S02,23),(B13,26)]`

Step 3: Poll S05 (jarak=10)
- Update: S06 = 10+8 = 18, sebelum[S06] = S05

Step 4: Poll B01 (jarak=10)
- Update: B11 = 10+12 = 22, sebelum[B11] = B01

Step 5: Poll S04 (jarak=12)
- Update: S08 = 12+7 = 19, sebelum[S08] = S04

... iterasi berlanjut sampai B06 di-poll ...

Step N: Poll B06 (jarak=20)
- B06 == tujuan -> SELESAI

Rekonstruksi path dari sebelum[]:
- B06 <- sebelum[B06]=S01 <- sebelum[S01]=S03
- Balik urutan: `[S03, S01, B06]`

Rute        : Stasiun Wonokromo -> Stasiun Surabaya Gubeng -> Halte Tunjungan
Total waktu : 20 menit

---

## 9. Screenshot Hasil Program
---
**Fitur 1**

<img width="608" height="926" alt="image" src="https://github.com/user-attachments/assets/b3ac5b3e-6908-4a86-a242-793f46cc4eb5" />

---
**Fitur 2**

<img width="1566" height="512" alt="image" src="https://github.com/user-attachments/assets/4a4a6fb3-d7d0-4db2-a829-1fc54d305254" />

---
**Fitur 3**

<img width="1314" height="502" alt="image" src="https://github.com/user-attachments/assets/fb7bc921-f76d-4c15-956a-c5d2d3b87027" />

---
**Fitur 4**

<img width="628" height="1048" alt="image" src="https://github.com/user-attachments/assets/4258bb7b-feb4-44aa-b000-e27807cffa14" />

---
**Fitur 5**

<img width="1492" height="582" alt="image" src="https://github.com/user-attachments/assets/31abd9de-1759-43c4-bda5-3d65deb18ec3" />

---
**Fitur 6**

<img width="996" height="1172" alt="image" src="https://github.com/user-attachments/assets/ff83f00b-ca6d-44d4-b8ba-9c68cf90ceda" />
<img width="996" height="756" alt="image" src="https://github.com/user-attachments/assets/0fd873a0-e379-4acd-a819-0fb945345cd4" />
<img width="790" height="616" alt="image" src="https://github.com/user-attachments/assets/55d610a1-5b4d-44f0-b9e7-283577763fb3" />


---
**Fitur 7**

<img width="708" height="990" alt="image" src="https://github.com/user-attachments/assets/1843ef60-f06c-481f-8d8b-35948434ed0a" />

---

## 10. Analisis Kompleksitas

| Fitur / Operasi | Kompleksitas Waktu | Kompleksitas Ruang | Penjelasan |
| :--- | :--- | :--- | :--- |
| **Trie - Insert** | O(L) | O(L) | **L** adalah panjang karakter nama halte/stasiun. Operasi pembuatan *node* berjalan sekuensial sesuai jumlah huruf. |
| **Trie - Search** | O(L + R) | O(R) | **L** adalah panjang *prefix* input, dan **R** adalah total node dari semua cabang subtree yang dikumpulkan secara rekursif saat memuat hasil *auto-complete*. |
| **MinHeap - Insert / Extract** | O(log N) | O(1) | **N** adalah jumlah elemen dalam *heap*. Proses *up-heapify* maupun *down-heapify* berjalan membelah *tree* secara logaritmik tanpa memakan memori tambahan. |
| **BFS (Minimum Transit)** | O(V + E) | O(V × P) | **V** = jumlah *vertex*, **E** = jumlah *edge*. Kompleksitas ruang memakan memori lebih besar karena algoritma ini menduplikasi dan menyimpan seluruh rekam jejak jalur (`List<String>`) di dalam antrean (`Queue`), di mana **P** adalah panjang rata-rata rute. |
| **Dijkstra (Tercepat/Termurah)**| O((V + E) log V)| O(V) | Mengeksplorasi graf menggunakan *Priority Queue* berbasis bobot. Pemakaian ruang jauh lebih efisien dari BFS (hanya O(V)) karena program hanya memetakan *pointer* *node* pada `HashMap` `sebelum` dan murni melakukan *backtracking* saat tujuan tercapai. |
| **Graph - Add Node / Edge** | O(1) *amortized* | O(1) | Penambahan data pada *Adjacency List* menggunakan fungsi bawaan `HashMap.put()` dan `List.add()` yang dieksekusi secara instan. |

---


## 11. What if analysis

1. **What if pengguna mencari rute ke halte yang berstatus "Nonaktif" (misal Halte Sidosermo - B16)?**
   * **Hasil:** Sistem langsung mengembalikan pesan peringatan "Rute tidak ditemukan."
   * **Analisis:** Karena logika `Graph.addEdge()` secara otomatis memblokir pembuatan *edge* jika asal atau tujuannya berstatus "Nonaktif", maka node tersebut memiliki *in-degree* murni nol. Algoritma `BFS` dan `Dijkstra` akan memproses penelusuran graf namun tidak akan pernah bisa mencapai node tujuan tersebut, sehingga terhindar dari *infinite loop*.
2. **What if beberapa rute dimatikan pada menu "Simulasi Rute Tidak Tersedia" sehingga jaringan graf terputus (terfragmentasi)?**
   * **Hasil:** Program mengembalikan output pencarian `ditemukan = false` dan memunculkan pesan "Tidak ada rute yang tersedia dengan kondisi simulasi saat ini."
   * **Analisis:** Pada setiap iterasi pengecekan tetangga, algoritma menerapkan *guard clause* `if (!edge.isAktif()) continue;`. Jika semua akses rute diputus, elemen di dalam struktur data `Queue` (pada BFS) atau `PriorityQueue` (pada Dijkstra) akan terus di-*poll* hingga kosong sepenuhnya tanpa pernah menyentuh kondisi *break* pada node tujuan. Program menangani antrean kosong ini dengan aman.
3. **What if pengguna mengetikkan nama stasiun asal/tujuan yang *typo* pada fitur pencarian (PrefixSearcher)?**
   * **Hasil:** Program seketika memunculkan notifikasi "Data tidak ditemukan."
   * **Analisis:** Implementasi `Trie` melakukan *lookup* dari level *root* ke bawah. Apabila ditemukan karakter salah yang tidak terdapat pada variabel `children`, pemanggilan `current.children.get(ch)` akan menghasilkan `null`. Program menangkap kondisi ini dan langsung me-*return* *list* kosong tanpa mengeksekusi rekursi berlebihan yang bisa memicu *NullPointerException*.
4. **What if pengguna memasukkan ID stasiun asal dan tujuan yang sama persis (misal asal: S01, tujuan: S01)?**
   * **Hasil:** Program akan langsung menampilkan stasiun tersebut sebagai *output* dengan keterangan bobot: 0 transit, 0 menit waktu, dan 0 rupiah biaya.
   * **Analisis:** Pada algoritma BFS, *Queue* diinisialisasi dengan asal (S01). Pada iterasi pertama, program langsung mendeteksi `sekarang == tujuan`, dan algoritma seketika berhenti. Ini membuktikan fungsionalitas penghentian dini (*early exit*) berjalan dengan baik tanpa perlu mengekspansi graf lebih lanjut.

---

## 12. Kesimpulan

Secara keseluruhan, proyek "Public Transport Planner" ini telah berhasil memodelkan simulasi jaringan transportasi umum secara efisien. Penggunaan struktur **Adjacency List** terbukti sangat ideal untuk merepresentasikan *sparse graph* jaringan kota, secara masif menghemat alokasi memori jika dibandingkan dengan *Adjacency Matrix* konvensional. Pada antarmuka interaksi, integrasi struktur data **Trie** sukses menyediakan fungsionalitas *auto-complete* yang secara drastis memangkas beban komputasi *string lookup*.

Sebagai inti penyelesaian masalah, program ini membedah pencarian rute melalui dua pendekatan algoritma utama: **Breadth-First Search (BFS)** yang beroperasi untuk memetakan *minimum transit* pada *unweighted graph*, serta algoritma **Dijkstra** yang memanfaatkan *Priority Queue* pada *weighted graph* untuk mengkalkulasi *shortest path* berdasarkan kriteria waktu dan biaya. Lebih jauh, arsitektur kode telah didesain dengan *robustness* yang tinggi; program mampu melakukan *early exit* saat input asal dan tujuan identik, menangani rute buntu, hingga beradaptasi dengan *fragmented graph* pada fitur simulasi pemutusan rute. Pada akhirnya, proyek ini tidak hanya mengimplementasikan teori dasar struktur data secara presisi, tetapi juga merangkainya menjadi *software* berbasis *Command Line Interface* (CLI) yang terintegrasi, interaktif, dan optimal.
