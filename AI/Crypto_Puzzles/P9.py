from itertools import permutations
from tabulate import tabulate

def disResult(equation):
    left, right = equation.split('=')
    left_words = left.split('+')
    return [word.strip() for word in left_words], right.strip()

def isValid(mapping, left_words, right_word):
    left_sum = sum(int(''.join(str(mapping[ch]) for ch in word)) for word in left_words)
    right_value = int(''.join(str(mapping[ch]) for ch in right_word))
    return left_sum == right_value

def solveCrypt(equation):
    left_words, right_word = disResult(equation)
    unique_chars = set(''.join(left_words) + right_word)
    if len(unique_chars) > 10:
        return None
    for perm in permutations(range(10), len(unique_chars)):
        mapping = dict(zip(unique_chars, perm))
        if any(mapping[word[0]] == 0 for word in left_words + [right_word]):
            continue
        if isValid(mapping, left_words, right_word):
            return mapping
    return None

def displayResult(equation, mapping):
    left_words, right_word = disResult(equation)
    mapped_left_words = [''.join(str(mapping[ch]) for ch in word) for word in left_words]
    mapped_right_value = ''.join(str(mapping[ch]) for ch in right_word)
    data = [
        ["Input Equation", equation],
        ["Mapped Values", " + ".join(mapped_left_words) + " = " + mapped_right_value],
        ["Result", "Valid Solution"]
    ]

    print(tabulate(data, headers="firstrow", tablefmt="grid"))

def main():
    equation = input("Enter the cryptarithmetic equation (SEND + MORE = MONEY): ")
    solution = solveCrypt(equation)

    if solution:
        print("Solution found:")
        for char, digit in solution.items():
            print(f"{char} -> {digit}")
        displayResult(equation, solution)
    else:
        print("No solution exists.")

if __name__ == "__main__":
    main()
