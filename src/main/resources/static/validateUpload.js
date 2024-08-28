document.addEventListener('DOMContentLoaded', () => {
    const openapiFileInput = document.getElementById('openapi-file');
    const validateButton = document.getElementById('validate-button');
    const uploadButton = document.getElementById('upload-button');
    const validationResult = document.getElementById('validation-result');
    const uploadResult = document.getElementById('upload-result');

    validateButton.addEventListener('click', async () => {
        const file = openapiFileInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = async (event) => {
                const yamlString = event.target.result;
                const validationMessage = await validateOpenAPIYAML(file, yamlString);
                validationResult.textContent = validationMessage;
            };
            reader.readAsText(file);
        } else {
            validationResult.textContent = 'Please select a YAML file to validate.';
        }
    });

    uploadButton.addEventListener('click', async () => {
        const file = openapiFileInput.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);

            try {
                const uploadResponse = await fetch('/api/upload', {
                    method: 'POST',
                    body: formData,
                });

                if (uploadResponse.ok) {
                    const uploadMessage = await uploadResponse.text();
                    uploadResult.textContent = uploadMessage;
                } else {
                    uploadResult.textContent = 'Upload failed. Server returned an error.';
                }
            } catch (error) {
                uploadResult.textContent = 'Upload failed. Error: ' + error.message;
            }
        } else {
            uploadResult.textContent = 'Please select a YAML file to upload.';
        }
    });

    // Function to validate OpenAPI YAML using Swagger online validator
    async function validateOpenAPIYAML(file, yamlString) {
        // Try validate yaml file format using Swagger.io validator API
        try {
            const response = await fetch('https://validator.swagger.io/validator/debug', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/yaml',
                },
                body: yamlString,
            });
            // Await validation response
            const result = await response.json();
            // If response message contains an error, return validation failed and describe the error
            if (result.schemaValidationMessages && result.schemaValidationMessages.length > 0) {
                const errorMessages = result.schemaValidationMessages.map((message) => message.message);
                return 'Validation failed. Errors:\n' + errorMessages.join('\n');
            } else {
                // If validation succeeded, return a success message
                return 'Validation successful: Valid OpenAPI YAML file.';
            }
        } catch (error) {
            return 'Validation failed. Error: ' + error.message;
        }
    }
});
