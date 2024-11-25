#include <bits/stdc++.h>
using namespace std;

mt19937_64 gen(chrono::steady_clock::now().time_since_epoch().count());
uniform_int_distribution<long long> rnd(0,LLONG_MAX);

#define ll long long

const int MAXN = 3e6 + 5; 
const int MAX_N = 15;
const int MOD = 1e9 + 7;
const int INF = 1e9;
const ll LINF = 1e18;

int n, x[MAX_N], y[MAX_N], adj[MAX_N][MAX_N], dp[1 << MAX_N][MAX_N];

ll tsp(ll mask, ll u) {
    if (mask == (1 << n) - 1) return adj[u][0]; // finish all nodes
    if (dp[mask][u] != -1) return dp[mask][u];

    ll ans = INF;
    for (ll v = 0; v < n; v++) {
        if (!(mask & (1 << v))) { // this node is unvisited
            ll cur = adj[u][v] + tsp(mask | (1 << v), v);
            ans = min(ans, cur);
        }
    }
    return dp[mask][u] = ans;
}

ll hsh[26];
vector<int> uniq;
ll comb[26];
int used[10];
int charFront[26];

bool bts(int sum, int cur) {
    if (cur == uniq.size()) {
        return (sum == 0);
    }
    int ch = uniq[cur];
    if (comb[ch] != -1) {
        bool check = bts(sum + hsh[ch] * comb[ch], cur + 1);
        if (check) return true;
    }
    for (int i = 9; i >= 0; i--) {
        if (charFront[ch] == 1 && i == 0) continue;
        if (used[i]) continue; 
        used[i] = 1;
        comb[ch] = i;
        bool check = bts(sum + hsh[ch] * i, cur + 1);
        if (check) return true;
        comb[ch] = -1;
        used[i] = 0;
    }
    return false;
}

void display_table(const vector<string>& st) {
    cout << "\nMapped Values:\n";
    cout << "Character | Value\n";
    cout << "------------------\n";
    for (int i = 0; i < 26; i++) {
        if (comb[i] != -1) {
            char ct = 'A' + i;
            cout << "    " << ct << "     |  " << comb[i] << endl;
        }
    }
    cout << "\nEquations:\n";
    for (const auto& s : st) {
        for (char c : s) {
            cout << comb[c - 'A'] << " ";
        }
        cout << endl;
    }
}

void sol() {
    ll a, b, c, n, m, k = -1, x, resu = 0;
    cin >> n;
    vector<string> st(n + 1);
    for (int i = 0; i <= n; i++) {
        cin >> st[i];
    }
    
    int ch = 0;
    ll exp = 0;
    uniq.clear();
    for (int i = 0; i < 26; i++) comb[i] = -1;
    
    for (int i = 0; i < n; i++) {
        exp = 1;
        for (int j = st[i].size() - 1; j >= 0; j--) {
            ch = st[i][j] - 'A';
            if (hsh[ch] == 0) uniq.push_back(ch);
            hsh[ch] += exp;
            exp *= 10;
            if (j == 0) charFront[ch] = 1;
        }
    }
    
    exp = 1;
    int i = n;
    for (int j = st[i].size() - 1; j >= 0; j--) {
        ch = st[i][j] - 'A';
        if (hsh[ch] == 0) uniq.push_back(ch);
        hsh[ch] -= exp;
        exp *= 10;
        if (j == 0) charFront[ch] = 1;
    }
    
    if (bts(0, 0)) {
        cout << "There exists a valid combination!\n";
        display_table(st);
    } else {
        cout << "No solution exists!\n";
    }
}

int main() {
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    int tc = 1;
    for (int t = 1; t <= tc; t++) {
        sol();
    }
    return 0;
}
