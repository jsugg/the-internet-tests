module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'body-max-line-length': [2, 'always', 100],
    'footer-max-line-length': [2, 'always', 100],
    'header-max-length': [2, 'always', 72],
    'scope-case': [2, 'always', 'lower-case'],
    'scope-enum': [
      2,
      'always',
      ['java', 'ts', 'py', 'catalog', 'docker', 'actions', 'deps', 'readme'],
    ],
    'subject-empty': [2, 'never'],
    'subject-full-stop': [2, 'never', '.'],
    'type-case': [2, 'always', 'lower-case'],
    'type-empty': [2, 'never'],
    'type-enum': [
      2,
      'always',
      ['feat', 'fix', 'docs', 'ci', 'build', 'refactor', 'test', 'chore', 'perf'],
    ],
  },
};
