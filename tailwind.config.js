/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#2C3E7A',
        secondary: '#E74C3C',
        accent: '#3498DB',
      }
    },
  },
  plugins: [],
}